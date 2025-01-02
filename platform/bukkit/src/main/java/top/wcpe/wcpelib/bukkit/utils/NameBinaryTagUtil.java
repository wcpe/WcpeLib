package top.wcpe.wcpelib.bukkit.utils;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NameBinaryTagUtil {

    public static Class<?> NBTTagCompoundClazz = Package.MINECRAFT_SERVER.getClass("NBTTagCompound");
    public static Class<?> NBTBaseClazz = Package.MINECRAFT_SERVER.getClass("NBTBase");
    public static Class<?> NBTTagIntClazz = Package.MINECRAFT_SERVER.getClass("NBTTagInt");
    public static Class<?> NBTTagStringClazz = Package.MINECRAFT_SERVER.getClass("NBTTagString");
    public static Class<?> NBTTagDoubleClazz = Package.MINECRAFT_SERVER.getClass("NBTTagDouble");
    public static Class<?> NBTTagShortClazz = Package.MINECRAFT_SERVER.getClass("NBTTagShort");
    public static Class<?> NBTTagFloatClazz = Package.MINECRAFT_SERVER.getClass("NBTTagFloat");
    public static Class<?> NBTTagListClazz = Package.MINECRAFT_SERVER.getClass("NBTTagList");
    public static Class<?> CraftItemStackClazz = Package.CRAFTBUKKIT.getClass("inventory.CraftItemStack");
    private static Method WRITE_NBT;
    private static Method READ_NBT;
    public Object compound;
    public Object nmsItem;

    public NameBinaryTagUtil() {
        this.compound = create(NBTTagCompoundClazz);
    }

    public NameBinaryTagUtil(Object compound) {
        this.compound = compound;
    }

    public NameBinaryTagUtil(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            this.compound = doStaticMethod(Package.MINECRAFT_SERVER.getClass("NBTCompressedStreamTools"), "a", new ParamGroup(fis, InputStream.class));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(NameBinaryTagUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static String toBase64(List<ItemStack> items) {
        ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream localDataOutputStream = new DataOutputStream(localByteArrayOutputStream);
        Object localNBTTagList = null;
        try {
            localNBTTagList = Package.MINECRAFT_SERVER.getClass("NBTTagList").getConstructor().newInstance();
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException |
                 SecurityException | InvocationTargetException e) {
            System.out.println("错误: " + e.getMessage());
        }
        try {
            for (ItemStack item : items) {
                if (item == null) {
                    continue;
                }
                Object localCraftItemStack = Package.CRAFTBUKKIT.getClass("inventory.CraftItemStack")
                        .getMethod("asCraftCopy", ItemStack.class).invoke(item, item);
                Object localNBTTagCompound = Package.MINECRAFT_SERVER.getClass("NBTTagCompound").getConstructor().newInstance();
                if (localCraftItemStack != null) {
                    try {
                        Object nmsItem = new NameBinaryTagUtil().readByItem(item).nmsItem;
                        nmsItem.getClass().getMethod("save", Package.MINECRAFT_SERVER.getClass("NBTTagCompound")).invoke(nmsItem,
                                localNBTTagCompound);
                    } catch (NullPointerException localNullPointerException) {
                        System.out.println("错误: " + localNullPointerException.getMessage());
                    }
                }
                localNBTTagList.getClass().getMethod("add", Package.MINECRAFT_SERVER.getClass("NBTBase")).invoke(localNBTTagList,
                        localNBTTagCompound);
            }
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException |
                 SecurityException | InvocationTargetException e) {
            System.out.println("错误: " + e.getMessage());
        }

        if (WRITE_NBT == null) {
            try {
                WRITE_NBT = Package.MINECRAFT_SERVER.getClass("NBTCompressedStreamTools").getDeclaredMethod("a",
                        Package.MINECRAFT_SERVER.getClass("NBTBase"), DataOutput.class);
                WRITE_NBT.setAccessible(true);
            } catch (NoSuchMethodException | SecurityException localException1) {
                throw new IllegalStateException("未找到写入方法", localException1);
            }
        }
        try {
            WRITE_NBT.invoke(null, localNBTTagList, localDataOutputStream);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException localException2) {
            throw new IllegalArgumentException("无法写入" + localNBTTagList + "至" + localDataOutputStream, localException2);
        }
        return Base64Coder.encodeLines(localByteArrayOutputStream.toByteArray());
    }

    public static List<ItemStack> fromBase64(String paramString) {
        ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(Base64Coder.decodeLines(paramString));
        Object localNBTTagList = readNbt(new DataInputStream(localByteArrayInputStream));
        List<ItemStack> arrayOfItemStack = new ArrayList<>();
        try {
            int size = (int) localNBTTagList.getClass().getMethod("size").invoke(localNBTTagList);
            for (int i = 0; i < size; i++) {
                Object localNBTTagCompound = localNBTTagList.getClass().getMethod("get", Integer.TYPE).invoke(localNBTTagList, i);
                if (!(boolean) localNBTTagCompound.getClass().getMethod("isEmpty").invoke(localNBTTagCompound)) {
                    String version = Package.getServerVersion();
                    int subVersion = Integer.valueOf(version.split("_")[1]);
                    if (subVersion >= 11) {
                        Constructor<?> constructor = Package.MINECRAFT_SERVER.getClass("ItemStack").getConstructor(Package.MINECRAFT_SERVER.getClass("NBTTagCompound"));
                        Object nmsItem = constructor.newInstance(localNBTTagCompound);
                        arrayOfItemStack.add((ItemStack) Package.CRAFTBUKKIT.getClass("inventory.CraftItemStack").getMethod("asCraftMirror", Package.MINECRAFT_SERVER.getClass("ItemStack")).invoke(nmsItem, nmsItem));
                    } else {
                        arrayOfItemStack.add((ItemStack) Package.CRAFTBUKKIT.getClass("inventory.CraftItemStack")
                                .getMethod("asCraftMirror", Package.MINECRAFT_SERVER.getClass("ItemStack"))
                                .invoke(localNBTTagCompound, Package.MINECRAFT_SERVER.getClass("ItemStack")
                                        .getMethod("createStack", Package.MINECRAFT_SERVER.getClass("NBTTagCompound"))
                                        .invoke(localNBTTagCompound, localNBTTagCompound)));
                    }
                }
            }

            return arrayOfItemStack;
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException |
                 SecurityException | InvocationTargetException e) {
            System.out.println("错误: " + e.getMessage());
        }
        return null;
    }

    private static Object readNbt(DataInput paramDataInput) {
        if (READ_NBT == null) {
            try {
                READ_NBT = Package.MINECRAFT_SERVER.getClass("NBTCompressedStreamTools").getDeclaredMethod("a",
                        DataInput.class, Integer.TYPE, Package.MINECRAFT_SERVER.getClass("NBTReadLimiter"));
                READ_NBT.setAccessible(true);
            } catch (NoSuchMethodException | SecurityException localException1) {
                throw new IllegalStateException("未找到方法.", localException1);
            }
        }
        try {
            Object limiter = Package.MINECRAFT_SERVER.getClass("NBTReadLimiter").getConstructor(Long.TYPE)
                    .newInstance(9223372036854775807L);
            return READ_NBT.invoke(null, paramDataInput, 0, limiter);
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException |
                 SecurityException | InvocationTargetException localException2) {
            throw new IllegalArgumentException("无法从该位置读取数据" + paramDataInput, localException2);
        }
    }

    public static Object create(Class<?> clazz, ParamGroup... args) {
        try {
            Class<?>[] types = new Class<?>[args.length];
            Object[] objs = new Object[args.length];
            for (int i = 0; i < args.length; i++) {
                types[i] = args[i].type;
                objs[i] = args[i].obj;
            }
            Constructor<?> cons = clazz.getConstructor(types);
            return cons.newInstance(objs);
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException |
                 IllegalArgumentException | InvocationTargetException ex) {
        }
        return null;
    }

    public static Object getField(Object obj, String fieldName) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (SecurityException | IllegalAccessException | IllegalArgumentException | NoSuchFieldException ex) {
        }
        return null;
    }

    public static Object doMethod(Object obj, String methodName, ParamGroup... args) {
        try {
            Class<?>[] types = new Class<?>[args.length];
            Object[] objs = new Object[args.length];
            for (int i = 0; i < args.length; i++) {
                types[i] = args[i].type;
                objs[i] = args[i].obj;
            }
            Method method = obj.getClass().getMethod(methodName, types);
            return method.invoke(obj, objs);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException |
                 InvocationTargetException ex) {
        }
        return null;
    }

    public static Object doStaticMethod(Class<?> clazz, String methodName, ParamGroup... args) {
        try {
            Class<?>[] types = new Class<?>[args.length];
            Object[] objs = new Object[args.length];
            for (int i = 0; i < args.length; i++) {
                types[i] = args[i].type;
                objs[i] = args[i].obj;
            }
            Method method = clazz.getMethod(methodName, types);
            return method.invoke(null, objs);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException |
                 InvocationTargetException ex) {
        }
        return null;
    }

    public void setInt(String key, Integer value) {
        if (value != null) {
            Object nms = create(NBTTagIntClazz, new ParamGroup(value, int.class));
            doMethod(compound, "set", new ParamGroup(key), new ParamGroup(nms, NBTBaseClazz));
        }
    }

    public void setString(String key, String value) {
        if (value != null) {
            Object nms = create(NBTTagStringClazz, new ParamGroup(value));
            doMethod(compound, "set", new ParamGroup(key), new ParamGroup(nms, NBTBaseClazz));
        }
    }

    public void setDouble(String key, Double value) {
        if (value != null) {
            Object nms = create(NBTTagDoubleClazz, new ParamGroup(value, double.class));
            doMethod(compound, "set", new ParamGroup(key), new ParamGroup(nms, NBTBaseClazz));
        }
    }

    public void setShort(String key, Short value) {
        if (value != null) {
            Object nms = create(NBTTagShortClazz, new ParamGroup(value, short.class));
            doMethod(compound, "set", new ParamGroup(key), new ParamGroup(nms, NBTBaseClazz));
        }
    }

    public void setFloat(String key, Float value) {
        if (value != null) {
            Object nms = create(NBTTagFloatClazz, new ParamGroup(value, float.class));
            doMethod(compound, "set", new ParamGroup(key), new ParamGroup(nms, NBTBaseClazz));
        }
    }

    public void setNBTList(String key, List<NameBinaryTagUtil> list) {
        Object nms = create(NBTTagListClazz);
        for (NameBinaryTagUtil nbt : list) {
            doMethod(nms, "add", new ParamGroup(nbt.compound, NBTBaseClazz));
        }
        doMethod(compound, "set", new ParamGroup(key, String.class), new ParamGroup(nms, NBTBaseClazz));
    }

    public Integer getInt(String key) {
        return ((Integer) doMethod(compound, "getInt", new ParamGroup(key)));
    }

    public String getString(String key) {
        return ((String) doMethod(compound, "getString", new ParamGroup(key)));
    }

    public Double getDouble(String key) {
        return ((Double) doMethod(compound, "getDouble", new ParamGroup(key)));
    }

    public Short getShort(String key) {
        return ((Short) doMethod(compound, "getShort", new ParamGroup(key)));
    }

    public Float getFloat(String key) {
        return ((Float) doMethod(compound, "getFloat", new ParamGroup(key)));
    }

    public Object get(String key) {
        return doMethod(compound, "get", new ParamGroup(key));
    }

    public Map getNBTList() {
        if (NetMinecraftServerUtil.getServerVersionNum() > 1170) {
            return (Map) getField(compound, "x");
        }
        return (Map) getField(compound, "map");
    }

    public List<NameBinaryTagUtil> getNBTList(String key) {
        List<NameBinaryTagUtil> nbts = new ArrayList<>();
        Map map = (Map) getField(compound, "map");
        Object list = map.get(key);
        if (list != null) {
            for (int i = 0; i < (int) doMethod(list, "size"); i++) {
                nbts.add(new NameBinaryTagUtil(doMethod(list, "get", new ParamGroup(i, int.class))));
            }
        }
        return nbts;
    }

    public ItemStack toItemStack() {
        nmsItem = create(Package.MINECRAFT_SERVER.getClass("ItemStack"), new ParamGroup(compound, NBTTagCompoundClazz));
        if (nmsItem == null) {
            nmsItem = doStaticMethod(Package.MINECRAFT_SERVER.getClass("ItemStack"), "createStack", new ParamGroup(compound, NBTTagCompoundClazz));
        }
        return (ItemStack) doStaticMethod(CraftItemStackClazz, "asBukkitCopy", new ParamGroup(nmsItem));
    }

    public NameBinaryTagUtil readByItem(ItemStack item) {
        nmsItem = doStaticMethod(CraftItemStackClazz, "asNMSCopy", new ParamGroup(item, ItemStack.class));
        Object hasTag = doMethod(nmsItem, "hasTag");
        compound = hasTag != null && ((Boolean) hasTag) ? doMethod(nmsItem, "getTag") : create(NBTTagCompoundClazz);
        return this;
    }

    public ItemStack writeToItemStack(ItemStack item) {
        if (item == null) {
            return null;
        }
        nmsItem = doStaticMethod(CraftItemStackClazz, "asNMSCopy", new ParamGroup(item, ItemStack.class));
        if (nmsItem == null) {
            return null;
        }
        doMethod(nmsItem, "setTag", new ParamGroup(compound, NBTTagCompoundClazz));
        return (ItemStack) doStaticMethod(CraftItemStackClazz, "asBukkitCopy", new ParamGroup(nmsItem));
    }

    public enum Package {
        MINECRAFT_SERVER("net.minecraft." + getNmsPacket()),
        CRAFTBUKKIT("org.bukkit.craftbukkit." + getServerVersion());

        private final String path;

        Package(String path) {
            this.path = path;
        }

        public static String getNmsPacket() {
            if (NetMinecraftServerUtil.getServerVersionNum() > 1170) {
                return "nbt";
            } else {
                return "server." + Bukkit.getServer().getClass().getPackage().getName().substring(23);
            }
        }

        public static String getServerVersion() {
            return Bukkit.getServer().getClass().getPackage().getName().substring(23);
        }

        public Class<?> getClass(String className) {
            try {
                return Class.forName(path + "." + className);
            } catch (ClassNotFoundException ex) {
            }
            return null;
        }
    }

    public static class ParamGroup {

        public Object obj;
        public Class<?> type;

        public ParamGroup(Object obj, Class<?> type) {
            this.obj = obj;
            this.type = type;
        }

        public ParamGroup(Object obj) {
            this.obj = obj;
            this.type = obj.getClass();
        }
    }
}
