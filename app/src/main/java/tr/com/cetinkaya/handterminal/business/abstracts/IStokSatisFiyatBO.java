package tr.com.cetinkaya.handterminal.business.abstracts;

import tr.com.cetinkaya.handterminal.models.Barkod;
import tr.com.cetinkaya.handterminal.models.Depo;
import tr.com.cetinkaya.handterminal.models.StokSatisFiyat;

public interface IStokSatisFiyatBO {
    StokSatisFiyat getIndirimliFiyat(Barkod barkod, Depo depo);
    StokSatisFiyat getEtiketFiyat(Barkod barkod, Depo depo);
    StokSatisFiyat getTakstiliFiyat(Barkod barkod, Depo depo);
}
