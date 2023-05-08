package pl.moderr.moderrkowo.core.bazar;

public interface GUICallback {

    void onOpen();
    void onClose();
    void onLeftClick(int slot);
    void onRightClick(int slot);

}
