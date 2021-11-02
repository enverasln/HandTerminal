package tr.com.cetinkaya.handterminal.daos.abstracts;

import tr.com.cetinkaya.handterminal.models.Kullanici;

public interface IKullaniciDao {
    int getCount();
    Kullanici getKullaniciWithKullaniciAdiAndSifre(String kullaniciAdi, String sifre);
}
