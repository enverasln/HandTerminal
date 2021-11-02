package tr.com.cetinkaya.handterminal.helpers.sdks.abstracts;

import tr.com.cetinkaya.handterminal.models.Barkod;
import tr.com.cetinkaya.handterminal.models.StokSatisFiyat;

public interface LabelPrintAdapter {
    void printKirmiziIndirimBarkodlu(Barkod barkod, StokSatisFiyat etiketFiyati, float indirimliFiyat);
    void printKirmiziIndirimBarkosuz(Barkod barkod, StokSatisFiyat etiketFiyati, float indirimliFiyat);
    void printBeyazFiyatEtiketiBarkodlu(Barkod barkod, StokSatisFiyat etiketFiyati);
    void printBeyazFiyatEtiketiBarkodsuz(Barkod barkod, StokSatisFiyat etiketFiyati);
    void printTaksitliFiyatEtiketi(Barkod barkod, float etiketFiyati, float taksitliFiyat);
    void printRafEtiketi(Barkod barkod, StokSatisFiyat etiketFiyati);
    void printIndirimEtiketi(Barkod barokd, StokSatisFiyat etiketFiyati, float indirimliFiyat);
}
