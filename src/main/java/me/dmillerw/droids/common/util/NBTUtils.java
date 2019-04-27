package me.dmillerw.droids.common.util;

import com.google.common.collect.Maps;
import net.minecraft.nbt.*;

import java.util.Map;

public class NBTUtils {

    public static class TagBuilder {

        private final Map<String, NBTBase> map = Maps.newHashMap();
        private NBTTagCompound baseTag;

        public TagBuilder() {
            this.baseTag = new NBTTagCompound();
        }

        public TagBuilder(NBTTagCompound baseTag) {
            this.baseTag = baseTag;
        }

        public NBTTagCompound build() {
            NBTTagCompound tagCompound = baseTag;
            map.forEach(tagCompound::setTag);
            return tagCompound;
        }

        private TagBuilder addTag(String key, NBTBase tag) {
            this.map.put(key, tag);
            return this;
        }

        public TagBuilder addByte(String key, byte _byte) {
            return this.addTag(key, new NBTTagByte(_byte));
        }

        public TagBuilder addShort(String key, short _short) {
            return this.addTag(key, new NBTTagShort(_short));
        }

        public TagBuilder addInt(String key, int _int) {
            return this.addTag(key, new NBTTagInt(_int));
        }

        public TagBuilder addLong(String key, long _long) {
            return this.addTag(key, new NBTTagLong(_long));
        }

        public TagBuilder addFloat(String key, float _float) {
            return this.addTag(key, new NBTTagFloat(_float));
        }

        public TagBuilder addDouble(String key, double _double) {
            return this.addTag(key, new NBTTagDouble(_double));
        }

        public TagBuilder addString(String key, String _string) {
            return this.addTag(key, new NBTTagString(_string));
        }

        public TagBuilder addCompoundTag(String key, NBTTagCompound _compound) {
            return this.addTag(key, _compound);
        }
    }

    public static TagBuilder tagBuilder() {
        return new TagBuilder();
    }

    public static TagBuilder tagBuilder(NBTTagCompound baseTag) {
        return new TagBuilder(baseTag);
    }
}
