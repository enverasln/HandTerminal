package tr.com.cetinkaya.handterminal.business.abstracts;

import tr.com.cetinkaya.handterminal.models.Kullanici;

public interface IKullaniciBO {
    int getCount();
    Kullanici getKullaniciWithKullaniciAdiAndSifre(String kullaniciAdi, String sifre);
}
