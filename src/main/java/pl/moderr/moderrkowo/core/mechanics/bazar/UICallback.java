package pl.moderr.moderrkowo.core.mechanics.bazar;

public interface UICallback {

    void onOpen();

    void onClose();

    void onLeftClick(int slot);

    void onRightClick(int slot);

}
