package top.wcpe.wcpelib.nukkit.npc.data;

import lombok.Data;
import top.wcpe.wcpelib.nukkit.npc.function.ClickNpcFunctional;

/**
 * Npc 数据类
 *
 * @author: WCPE 1837019522@qq.com
 * @create: 2021-09-01 09:10
 */
@Data
public class NpcData {
    private String locationToString;
    private String uuid;
    private String name;
    private String skinName;
    private boolean rotation;
    private ClickNpcFunctional clickNpcFunctional;

    public NpcData(String locationToString, String uuid, String name, String skinName, boolean rotation) {
        this.locationToString = locationToString;
        this.uuid = uuid;
        this.name = name;
        this.skinName = skinName;
        this.rotation = rotation;
    }

}
