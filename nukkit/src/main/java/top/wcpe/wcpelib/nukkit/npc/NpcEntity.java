package top.wcpe.wcpelib.nukkit.npc;

import java.util.Map;

import cn.nukkit.IPlayer;
import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.inventory.InventoryHolder;
import cn.nukkit.lang.TextContainer;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.AddPlayerPacket;
import cn.nukkit.permission.Permission;
import cn.nukkit.permission.PermissionAttachment;
import cn.nukkit.permission.PermissionAttachmentInfo;
import cn.nukkit.plugin.Plugin;
import lombok.Getter;
import top.wcpe.wcpelib.nukkit.WcpeLib;

/**
 * Npc 实体类
 *
 * @author: WCPE 1837019522@qq.com
 * @create: 2021-09-1 9:08
 */
public class NpcEntity extends EntityHuman implements CommandSender, InventoryHolder, IPlayer {

    @Getter
    private String uuid;

    public NpcEntity(FullChunk chunk, CompoundTag nbt, String uuid) {
        super(chunk, nbt);
        this.uuid = uuid;
    }

    @Override
    public void spawnTo(Player player) {
        if (!this.hasSpawned.containsKey(player.getLoaderId())) {
            this.hasSpawned.put(player.getLoaderId(), player);

            skin.setTrusted(true);
            this.server.updatePlayerListData(this.getUniqueId(), this.getId(), this.getName(), this.skin,
                    new Player[]{player});

            AddPlayerPacket pk = new AddPlayerPacket();
            pk.uuid = this.getUniqueId();
            pk.username = this.getName();
            pk.entityUniqueId = this.getId();
            pk.entityRuntimeId = this.getId();
            pk.x = (float) this.x;
            pk.y = (float) this.y;
            pk.z = (float) this.z;
            pk.speedX = (float) this.motionX;
            pk.speedY = (float) this.motionY;
            pk.speedZ = (float) this.motionZ;
            pk.yaw = (float) this.yaw;
            pk.pitch = (float) this.pitch;
            pk.metadata = this.dataProperties;
            player.dataPacket(pk);
            this.server.removePlayerListData(this.getUniqueId(), new Player[]{player});
            super.spawnTo(player);
        }
    }

    @Override
    public boolean attack(EntityDamageEvent source) {
        return false;
    }

    @Override
    public boolean attack(float damage) {
        return false;
    }

    // XXX IPlayer
    @Override
    public boolean isOp() {
        return false;
    }

    @Override
    public void setOp(boolean value) {
    }

    @Override
    public boolean isOnline() {
        return true;
    }

    @Override
    public boolean isBanned() {
        return false;
    }

    @Override
    public void setBanned(boolean value) {
    }

    @Override
    public boolean isWhitelisted() {
        return true;
    }

    @Override
    public void setWhitelisted(boolean value) {
    }

    @Override
    public Player getPlayer() {
        return null;
    }

    @Override
    public Long getFirstPlayed() {
        return null;
    }

    @Override
    public Long getLastPlayed() {
        return null;
    }

    @Override
    public boolean hasPlayedBefore() {
        return false;
    }

    // XXX IPlayer
    // XXX CommandSender
    @Override
    public boolean isPermissionSet(String name) {
        return false;
    }

    @Override
    public boolean isPermissionSet(Permission permission) {
        return false;
    }

    @Override
    public boolean hasPermission(String name) {
        return false;
    }

    @Override
    public boolean hasPermission(Permission permission) {
        return false;
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin) {
        return null;
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name) {
        return null;
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, Boolean value) {
        return null;
    }

    @Override
    public void removeAttachment(PermissionAttachment attachment) {
    }

    @Override
    public void recalculatePermissions() {
    }

    @Override
    public Map<String, PermissionAttachmentInfo> getEffectivePermissions() {
        return null;
    }

    @Override
    public void sendMessage(String message) {

    }

    @Override
    public void sendMessage(TextContainer message) {

    }

    @Override
    public boolean isPlayer() {
        return false;
    }
    // XXX CommandSender
}
