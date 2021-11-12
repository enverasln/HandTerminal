package tr.com.cetinkaya.handterminal.business.abstracts;

import tr.com.cetinkaya.handterminal.models.Barkod;

public interface IBarkodBO {
    Barkod getBarkodWithBarkod(String barkodu);

    String getLastupDate();

    int updateBarkod(Barkod newBarkod);

    void insertBarkod(Barkod newBarkod);
}
