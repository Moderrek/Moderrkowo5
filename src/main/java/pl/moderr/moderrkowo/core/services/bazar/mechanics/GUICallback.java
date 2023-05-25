package pl.moderr.moderrkowo.core.services.bazar.mechanics;

public interface GUICallback {

    void onOpen();

    void onClose();

    void onLeftClick(int slot);

    void onRightClick(int slot);

    void onShiftClick(int slot);

}
