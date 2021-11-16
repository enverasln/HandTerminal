package tr.com.cetinkaya.handterminal.business.concretes;

import tr.com.cetinkaya.handterminal.business.abstracts.IStokSatisFiyatBO;
import tr.com.cetinkaya.handterminal.daos.abstracts.IBarkodDao;
import tr.com.cetinkaya.handterminal.daos.abstracts.IStokSatisFiyatDao;
import tr.com.cetinkaya.handterminal.models.Barkod;
import tr.com.cetinkaya.handterminal.models.Depo;
import tr.com.cetinkaya.handterminal.models.StokSatisFiyat;

public class StokSatisFiyatBO implements IStokSatisFiyatBO {

    private IStokSatisFiyatDao satisFiyatDao;

    public StokSatisFiyatBO(IStokSatisFiyatDao satisFiyatDao) {
        this.satisFiyatDao = satisFiyatDao;
    }

    @Override
    public StokSatisFiyat getIndirimliFiyat(Barkod barkod, Depo depo) {
        return satisFiyatDao.getIndirimliFiyat(barkod, depo);
    }

    @Override
    public StokSatisFiyat getEtiketFiyat(Barkod barkod, Depo depo) {
        return satisFiyatDao.getEtiketFiyat(barkod, depo);
    }

    @Override
    public StokSatisFiyat getTakstiliFiyat(Barkod barkod, Depo depo) {
        return satisFiyatDao.getTakstiliFiyat(barkod, depo);
    }

    @Override
    public String getLastupDate() {
        return satisFiyatDao.getLastupDate();
    }

    @Override
    public int updateSatifFiyat(StokSatisFiyat newSFiyat) {
        return satisFiyatDao.updateSatifFiyat(newSFiyat);
    }

    @Override
    public void insertSatisFiyat(StokSatisFiyat newSFiyat) {
        satisFiyatDao.insertSatisFiyat(newSFiyat);
    }
}
