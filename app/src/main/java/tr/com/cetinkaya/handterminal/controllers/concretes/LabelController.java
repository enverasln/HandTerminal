package tr.com.cetinkaya.handterminal.controllers.concretes;

import tr.com.cetinkaya.handterminal.business.abstracts.IBarkodBO;

import tr.com.cetinkaya.handterminal.business.abstracts.IDepoBO;
import tr.com.cetinkaya.handterminal.business.abstracts.IStokSatisFiyatBO;
import tr.com.cetinkaya.handterminal.controllers.abstracts.ILabelController;
import tr.com.cetinkaya.handterminal.dtos.LabelDto;
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
        Depo satisDepo = depoBO.getDepoById(depoNo);

        if(brkd != null) {
            String stokKod = brkd.getStok().getSto_kod();
            String stokAdi = brkd.getStok().getSto_isim();
            String stokKisaAdi = brkd.getStok().getSto_kisa_ismi();
            String reyon = brkd.getStok().getSto_reyon_kodu();
            String beden = brkd.getBar_bedennumarasi();
            StokSatisFiyat etiketFiyat = satisFiyatBO.getEtiketFiyat(brkd, etiketDepo);
            StokSatisFiyat satisFiyati = satisFiyatBO.getDepoFiyati(brkd, satisDepo);
            StokSatisFiyat taksitliFiyat = satisFiyatBO.getTakstiliFiyat(brkd, satisDepo);

            return new LabelDto.Builder(barkod, stokKod, stokAdi, stokKisaAdi, satisFiyati.getSfiyat_fiyati(), reyon)
                    .beden(beden)
                    .etiketFiyati(etiketFiyat.getSfiyat_fiyati())
                    .taksitFiyati(taksitliFiyat.getSfiyat_fiyati())
                    .birimAdi(brkd.getStok().getSto_birim3_ad())
                    .birimKatSayi(brkd.getStok().getSto_birim3_katsayi())
                    .yerliUretim(brkd.getStok().getSto_yerli_yabanci())
                    .fiyatDegTarihi(satisFiyati.getSfiyat_lastup_date())
                    .mensei(brkd.getStok().getSto_mensei())
                    .build();
        }

        return null;
    }
}
