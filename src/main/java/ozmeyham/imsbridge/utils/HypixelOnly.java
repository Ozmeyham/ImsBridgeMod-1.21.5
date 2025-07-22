package ozmeyham.imsbridge.utils;

import static ozmeyham.imsbridge.IMSBridge.onHypixel;
import static ozmeyham.imsbridge.utils.TextUtils.printToChat;

public class HypixelOnly {
    public static boolean requireHypixel(Boolean sendmsg) { //that sendmsg was unnecessary i forgot why i implemented it. too lazy to remove
        if (!onHypixel) {
            if (sendmsg) {printToChat("§cThis command only works on Hypixel!");}
            return false;
        }
        return true;
    }
}
