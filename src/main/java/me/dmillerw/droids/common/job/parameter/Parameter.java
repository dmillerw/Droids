package me.dmillerw.droids.common.job.parameter;

public class Parameter {

    public static enum Type {

        NUMBER, BLOCK_POS, AREA, ITEM, BLOCK;

        public static Type fromString(String string) {
            for (Type type : values()) {
                if (type.name().equalsIgnoreCase(string)) {
                    return type;
                }
            }

            return null;
        }
    }

    public final ParameterKey key;
    public final String description;

    public Parameter(ParameterKey key, String description) {
        this.key = key;
        this.description = description;
    }
}
