package tr.com.cetinkaya.handterminal.helpers;

import java.io.Serializable;

import tr.com.cetinkaya.handterminal.R;

public enum BarkodTipi implements Serializable {
    BARKODSUZ_KIRMIZI_INDIRIM,
    BARKODLU_KIRMIZI_INDIRIM,
    BARKODSUZ_BEYAZ,
    BARKODLU_BEYAZ,
    BARKODSUZ_TAKSITLI_FIYAT,
    BARKODLU_TAKSITLI_FIYAT,
    INDIRIMLI_RAF,
    INDIRIMSIZ_RAF,
}
