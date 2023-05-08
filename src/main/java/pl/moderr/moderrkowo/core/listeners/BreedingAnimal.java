package pl.moderr.moderrkowo.core.listeners;

import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

public enum BreedingAnimal {
    HORSE, SHEEP, COW, MUSHROOM_COW, PIG, CHICKEN, RABBIT;

    @Contract(pure = true)
    @Nullable
    public EntityType toEntity() {
        switch (this) {
            case HORSE:
                return EntityType.HORSE;
            case SHEEP:
                return EntityType.SHEEP;
            case COW:
                return EntityType.COW;
            case PIG:
                return EntityType.PIG;
            case RABBIT:
                return EntityType.RABBIT;
            case CHICKEN:
                return EntityType.CHICKEN;
            case MUSHROOM_COW:
                return EntityType.MUSHROOM_COW;
            default:
                return null;
        }
    }
}
