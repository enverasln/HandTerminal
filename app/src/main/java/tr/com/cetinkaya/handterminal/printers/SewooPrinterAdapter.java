package tr.com.cetinkaya.handterminal.printers;

import com.sewoo.port.android.BluetoothPort;
import com.sewoo.request.android.RequestHandler;

import java.io.Serializable;

public class SewooPrinterAdapter extends IPrinterAdapter {


    public enum BarkodTipi implements Serializable {
        BARKODSUZ_KIRMIZI_INDIRIM,
        BARKODLU_KIRMIZI_INDIRIM,
        BARKODSUZ_BEYAZ,
        BARKODLU_BEYAZ,
        INDIRIMLI_RAF,
        INDIRIMSIZ_RAF
    }

    public void printBarkodsuzKırmızıEtiket() {

    }
}
