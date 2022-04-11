package top.wcpe.wcpelib.bukkit.version


/**
 * 由 WCPE 在 2022/4/1 16:00 创建
 *
 * Created by WCPE on 2022/4/1 16:00
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.0.10-alpha-dev-4
 */
enum class VersionInfo(
    open val versionNumber: Int,
    open val nmsClassPath: String,
    open val obcClassPath: String,
) {
    Unknown(-1, "Unknown", "Unknown"),
    V1_8_8(188, "net.minecraft.server.v1_8_R3", "org.bukkit.craftbukkit.v1_8_R3"),
    V1_9_0(190, "net.minecraft.server.v1_9_R1", "org.bukkit.craftbukkit.v1_9_R1"),
    V1_9_2(192, "net.minecraft.server.v1_9_R1", "org.bukkit.craftbukkit.v1_9_R1"),
    V1_9_4(194, "net.minecraft.server.v1_9_R2", "org.bukkit.craftbukkit.v1_9_R2"),
    V1_11_2(1112, "net.minecraft.server.v1_11_R1", "org.bukkit.craftbukkit.v1_11_R1"),
    V1_12_1(1121, "net.minecraft.server.v1_12_R1", "org.bukkit.craftbukkit.v1_12_R1"),
    V1_12_2(1122, "net.minecraft.server.v1_12_R1", "org.bukkit.craftbukkit.v1_12_R1"),
    V1_13_2(1132, "net.minecraft.server.v1_13_R1", "org.bukkit.craftbukkit.v1_13_R1"),
    V1_14_1(1141, "net.minecraft.server.v1_14_R1", "org.bukkit.craftbukkit.v1_14_R1"),
    V1_14_2(1142, "net.minecraft.server.v1_14_R1", "org.bukkit.craftbukkit.v1_14_R1"),
    V1_14_3(1143, "net.minecraft.server.v1_14_R1", "org.bukkit.craftbukkit.v1_14_R1"),
    V1_14_4(1144, "net.minecraft.server.v1_14_R1", "org.bukkit.craftbukkit.v1_14_R1"),
    V1_15_1(1151, "net.minecraft.server.v1_15_R1", "org.bukkit.craftbukkit.v1_15_R1"),
    V1_15_2(1152, "net.minecraft.server.v1_15_R1", "org.bukkit.craftbukkit.v1_15_R1"),
    V1_16_1(1161, "net.minecraft.server.v1_16_R1", "org.bukkit.craftbukkit.v1_16_R1"),
    V1_16_2(1162, "net.minecraft.server.v1_16_R2", "org.bukkit.craftbukkit.v1_16_R2"),
    V1_16_3(1163, "net.minecraft.server.v1_16_R2", "org.bukkit.craftbukkit.v1_16_R2"),
    V1_16_4(1164, "net.minecraft.server.v1_16_R3", "org.bukkit.craftbukkit.v1_16_R3"),
    V1_16_5(1165, "net.minecraft.server.v1_16_R3", "org.bukkit.craftbukkit.v1_16_R3"),
    V1_17_1(1171, "net.minecraft", "org.bukkit.craftbukkit.v1_17_R1"),
    V1_18_1(1181, "net.minecraft", "org.bukkit.craftbukkit.v1_18_R1"),
    V1_18_2(1182, "net.minecraft", "org.bukkit.craftbukkit.v1_18_R2");

    open fun getNmsClass(className: String): Class<*> {
        return Class.forName("$nmsClassPath.$className")
    }

}