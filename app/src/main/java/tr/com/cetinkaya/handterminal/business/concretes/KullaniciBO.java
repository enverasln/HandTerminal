package tr.com.cetinkaya.handterminal.business.concretes;

import tr.com.cetinkaya.handterminal.business.abstracts.IKullaniciBO;
import tr.com.cetinkaya.handterminal.daos.abstracts.IKullaniciDao;
import tr.com.cetinkaya.handterminal.models.Kullanici;

public class KullaniciBO implements IKullaniciBO {
    private IKullaniciDao kullaniciDao;

    public KullaniciBO(IKullaniciDao kullaniciDao) {
        this.kullaniciDao = kullaniciDao;
    }

    @Override
    public int getCount() {
        return kullaniciDao.getCount();
    }

    @Override
    public Kullanici getKullaniciWithKullaniciAdiAndSifre(String kullaniciAdi, String sifre) {
        return  kullaniciDao.getKullaniciWithKullaniciAdiAndSifre(kullaniciAdi, sifre);
    }
}
