package me.dmillerw.droids.common.job.parameter;

import java.util.Objects;

public class ParameterKey {

    public static ParameterKey of(Parameter.Type type, String key) {
        return new ParameterKey(type, key);
    }

    public final Parameter.Type type;
    public final String key;

    private ParameterKey(Parameter.Type type, String key) {
        this.type = type;
        this.key = key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParameterKey that = (ParameterKey) o;
        return Objects.equals(key, that.key) &&
                type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, type);
    }
}
