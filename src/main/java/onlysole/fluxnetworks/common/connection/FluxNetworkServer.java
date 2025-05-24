package onlysole.fluxnetworks.common.connection;

import onlysole.fluxnetworks.FluxConfig;
import onlysole.fluxnetworks.FluxNetworks;
import onlysole.fluxnetworks.api.network.*;
import onlysole.fluxnetworks.api.tiles.IFluxConnector;
import onlysole.fluxnetworks.api.tiles.IFluxPlug;
import onlysole.fluxnetworks.api.tiles.IFluxPoint;
import onlysole.fluxnetworks.api.utils.Capabilities;
import onlysole.fluxnetworks.api.utils.EnergyType;
import onlysole.fluxnetworks.common.connection.transfer.FluxControllerHandler;
import onlysole.fluxnetworks.common.core.FluxUtils;
import onlysole.fluxnetworks.common.event.FluxConnectionEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import onlysole.fluxnetworks.common.util.FluxEnvironment;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class represents a Flux Network on logical server
 */
public class FluxNetworkServer extends FluxNetworkBase implements IFluxNetwork, IStellarFluxNetwork {

    private final Map<FluxLogicType, List<? extends IFluxConnector>> connections = new EnumMap<>(FluxLogicType.class);

    private final Queue<IFluxConnector> toAdd = new LinkedList<>();
    private final Queue<IFluxConnector> toRemove = new LinkedList<>();

    public boolean sortConnections = true;

    private final List<PriorityGroup<IFluxPlug>> sortedPlugs = new ArrayList<>();
    private final List<PriorityGroup<IFluxPoint>> sortedPoints = new ArrayList<>();

    private final TransferIterator<IFluxPlug> plugTransferIterator = new TransferIterator<>(false);
    private final TransferIterator<IFluxPoint> pointTransferIterator = new TransferIterator<>(true);

    public long bufferLimiter = 0;

    public FluxNetworkServer() {
        super();
    }

    public FluxNetworkServer(int id, String name, SecurityType security, int color, UUID owner, EnergyType energy, String password) {
        super(id, name, security, color, owner, energy, password);
    }

    private void handleConnectionQueue() {
        IFluxConnector device;
        while ((device = toAdd.poll()) != null) {
            for (FluxLogicType type : FluxLogicType.getValidTypes(device)) {
                sortConnections |= FluxUtils.addWithCheck(getConnections(type), device);
            }
            MinecraftForge.EVENT_BUS.post(new FluxConnectionEvent.Connected(device, this));
        }
        while ((device = toRemove.poll()) != null) {
            for (FluxLogicType type : FluxLogicType.getValidTypes(device)) {
                sortConnections |= getConnections(type).remove(device);
            }
        }
        if (sortConnections) {
            sortConnections();
            sortConnections = false;
        }
    }

    @Override
    public Runnable getCycleStartRunnable() {
        handleConnectionQueue();
        return () -> {
            List<IFluxConnector> devices = getConnections(FluxLogicType.ANY);
            devices.parallelStream().forEach(device -> {
                try {
                    ITransferHandler handler = device.getTransferHandler();
                    if (handler instanceof FluxControllerHandler) {
                        synchronized (FluxControllerHandler.class) {
                            handler.onCycleStart();
                        }
                    } else {
                        handler.onCycleStart();
                    }
                } catch (Throwable e) {
                    FluxNetworks.logger.warn("[FluxNetworkServer] Execute transfer handler `onCycleStart` failed.", e);
                }
            });
        };
    }

    @Nonnull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends IFluxConnector> List<T> getConnections(FluxLogicType type) {
        return (List<T>) connections.computeIfAbsent(type, m -> new ArrayList<>());
    }

    @Override
    public void onEndServerTick() {
        network_stats.getValue().startProfiling();

        bufferLimiter = 0;

        List<IFluxConnector> devices = getConnections(FluxLogicType.ANY);

        if (!FluxConfig.general.parallelNetworkCalculation || !FluxEnvironment.shouldParallel()) {
            handleConnectionQueue();
            for (IFluxConnector f : devices) {
                f.getTransferHandler().onCycleStart();
            }
        }

        if (!sortedPoints.isEmpty() && !sortedPlugs.isEmpty()) {
            plugTransferIterator.reset(sortedPlugs);
            pointTransferIterator.reset(sortedPoints);
            CYCLE:
            while (pointTransferIterator.hasNext()) {
                while (plugTransferIterator.hasNext()) {
                    IFluxPlug plug = plugTransferIterator.next();
                    IFluxPoint point = pointTransferIterator.next();
                    if (plug.getConnectionType() == point.getConnectionType()) {
                        break CYCLE; // Storage always have the lowest priority, the cycle can be broken here.
                    }
                    // we don't need to simulate this action
                    long operate = plug.getTransferHandler().removeFromBuffer(point.getTransferHandler().getRequest());
                    if (operate > 0) {
                        point.getTransferHandler().addToBuffer(operate);
                        continue CYCLE;
                    } else {
                        // although the plug still need transfer (buffer > 0)
                        // but it reached max transfer limit, so we use next plug
                        plugTransferIterator.incrementFlux();
                    }
                }
                break; // all plugs have been used
            }
        }
        for (IFluxConnector f : devices) {
            f.getTransferHandler().onCycleEnd();
            bufferLimiter += f.getTransferHandler().getRequest();
        }

        network_stats.getValue().stopProfiling();
    }

    @Override
    public AccessLevel getMemberPermission(EntityPlayer player) {
        if (!FluxConfig.general.synchronize) {
            if (FluxConfig.networks.enableSuperAdmin) {
                ISuperAdmin sa = player.getCapability(Capabilities.SUPER_ADMIN, null);
                if (sa != null && sa.getPermission()) {
                    return AccessLevel.SUPER_ADMIN;
                }
            }
            return network_players.getValue().stream().collect(Collectors.toMap(NetworkMember::getPlayerUUID, NetworkMember::getAccessPermission)).getOrDefault(EntityPlayer.getUUID(player.getGameProfile()), network_security.getValue().isEncrypted() ? AccessLevel.NONE : AccessLevel.USER);


        } else {
            UUID uuid = EntityPlayer.getUUID(player.getGameProfile());
            synchronized (network_players) {
                for (final NetworkMember member : network_players.getValue()) {
                    if (member.getPlayerUUID().equals(uuid)) {
                        return (member.getAccessPermission());
                    }
                }
                return (network_security.getValue().isEncrypted() ? AccessLevel.NONE : AccessLevel.USER);
            }
        }
    }

    @Override
    public void onRemoved() {
        getConnections(FluxLogicType.ANY).forEach(flux -> MinecraftForge.EVENT_BUS.post(new FluxConnectionEvent.Disconnected(flux, this)));
        connections.clear();
        toAdd.clear();
        toRemove.clear();
        sortedPoints.clear();
        sortedPlugs.clear();
    }

    @Override
    public void queueConnectionAddition(IFluxConnector flux) {
        toAdd.add(flux);
        toRemove.remove(flux);
        addToLite(flux);
    }

    @Override
    public void queueConnectionRemoval(IFluxConnector flux, boolean chunkUnload) {
        toRemove.add(flux);
        toAdd.remove(flux);
        if (chunkUnload) {
            changeChunkLoaded(flux, false);
        } else {
            removeFromLite(flux);
        }
    }

    private void addToLite(IFluxConnector flux) {
        Optional<IFluxConnector> c = all_connectors.getValue().stream().filter(f -> f.getCoords().equals(flux.getCoords())).findFirst();
        if (c.isPresent()) {
            changeChunkLoaded(flux, true);
        } else {
            FluxLiteConnector lite = new FluxLiteConnector(flux);
            all_connectors.getValue().add(lite);
        }
    }

    private void removeFromLite(IFluxConnector flux) {
        all_connectors.getValue().removeIf(f -> f.getCoords().equals(flux.getCoords()));
    }

    private void changeChunkLoaded(IFluxConnector flux, boolean chunkLoaded) {
        Optional<IFluxConnector> c = all_connectors.getValue().stream().filter(f -> f.getCoords().equals(flux.getCoords())).findFirst();
        c.ifPresent(fluxConnector -> fluxConnector.setChunkLoaded(chunkLoaded));
    }

    @Override
    public void addNewMember(String name) {
        NetworkMember newMember = NetworkMember.createMemberByUsername(name);
        if (!FluxConfig.general.synchronize) {
            if (network_players.getValue().stream().noneMatch(f -> f.getPlayerUUID().equals(newMember.getPlayerUUID()))) {
                network_players.getValue().add(newMember);
            }
        } else {
            synchronized (network_players) {
                List<NetworkMember> players = network_players.getValue();

                for (final NetworkMember player : players) {
                    if (player.getPlayerUUID().equals(newMember.getPlayerUUID())) {
                        return;
                    }
                }

                players.add(newMember);
            }
        }
    }

    @Override
    public void removeMember(UUID uuid) {
        if (!FluxConfig.general.synchronize) {
            network_players.getValue().removeIf(p -> p.getPlayerUUID().equals(uuid) && !p.getAccessPermission().canDelete());
        } else {
            synchronized (network_players) {
                network_players.getValue().removeIf(member -> member.getPlayerUUID().equals(uuid) && !member.getAccessPermission().canDelete());
            }
        }
    }

    @Override
    public Optional<NetworkMember> getValidMember(UUID player) {
        if (!FluxConfig.general.synchronize) {
            return network_players.getValue().stream().filter(f -> f.getPlayerUUID().equals(player)).findFirst();
        } else {
            synchronized (network_players) {
                return network_players.getValue().stream().filter(f -> f.getPlayerUUID().equals(player)).findFirst();
            }
        }
    }

    public void markLiteSettingChanged(IFluxConnector flux) {

    }

    private void sortConnections() {
        sortedPlugs.clear();
        sortedPoints.clear();
        List<IFluxPlug> plugs = getConnections(FluxLogicType.PLUG);
        List<IFluxPoint> points = getConnections(FluxLogicType.POINT);
        plugs.forEach(p -> PriorityGroup.getOrCreateGroup(p.getLogicPriority(), sortedPlugs).getConnectors().add(p));
        points.forEach(p -> PriorityGroup.getOrCreateGroup(p.getLogicPriority(), sortedPoints).getConnectors().add(p));
        sortedPlugs.sort(Comparator.comparing(p -> -p.getPriority()));
        sortedPoints.sort(Comparator.comparing(p -> -p.getPriority()));
    }
}
