package tr.com.cetinkaya.handterminal.controllers.concretes;

import tr.com.cetinkaya.handterminal.business.abstracts.IBarkodBO;

import tr.com.cetinkaya.handterminal.business.abstracts.IDepoBO;
import tr.com.cetinkaya.handterminal.business.abstracts.IStokSatisFiyatBO;
import tr.com.cetinkaya.handterminal.controllers.abstracts.ILabelController;
import tr.com.cetinkaya.handterminal.dtos.LabelDto;
import tr.com.cetinkaya.handterminal.helpers.BarkodTipi;
import tr.com.cetinkaya.handterminal.models.Barkod;
import tr.com.cetinkaya.handterminal.models.Depo;
import tr.com.cetinkaya.handterminal.models.StokSatisFiyat;

public class LabelController implements ILabelController {
    private IBarkodBO barkodBO;
    private IDepoBO depoBO;
    private IStokSatisFiyatBO satisFiyatBO;

    public LabelController(IBarkodBO barkodBO, IDepoBO depoBO, IStokSatisFiyatBO satisFiyatBO) {
        this.barkodBO = barkodBO;
        this.depoBO = depoBO;
        this.satisFiyatBO = satisFiyatBO;
    }



    @Override
    public LabelDto getLabel(String barkod, int etiketDepoNo, int depoNo) {

        Barkod brkd = barkodBO.getBarkodWithBarkod(barkod);
        Depo etiketDepo = depoBO.getDepoById(etiketDepoNo);
        Depo depo = depoBO.getDepoById(depoNo);

        if(brkd != null) {
            String stokKod = brkd.getStok().getSto_kod();
            String stokAdi = brkd.getStok().getSto_isim();
            String reyon = brkd.getStok().getSto_reyon_kodu();
            String beden = brkd.getBar_bedennumarasi();
            StokSatisFiyat etiketFiyat = satisFiyatBO.getEtiketFiyat(brkd, etiketDepo);
            StokSatisFiyat indirimliFiyat = satisFiyatBO.getIndirimliFiyat(brkd, depo);
            StokSatisFiyat taksitliFiyat = satisFiyatBO.getTakstiliFiyat(brkd, depo);




            return new LabelDto.Builder(stokKod, stokAdi, etiketFiyat.getSfiyat_fiyati(), reyon)
                    .beden(beden)
                    .indirimFiyati(indirimliFiyat.getSfiyat_fiyati())
                    .taksitFiyati(taksitliFiyat.getSfiyat_fiyati())
                    .birimAdi(brkd.getStok().getSto_birim3_ad())
                    .birimKatSayi(brkd.getStok().getSto_birim3_katsayi())
                    .build();
        }

        return null;
    }
}
