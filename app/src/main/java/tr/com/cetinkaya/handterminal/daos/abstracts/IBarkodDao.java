package tr.com.cetinkaya.handterminal.daos.abstracts;

import tr.com.cetinkaya.handterminal.models.Barkod;

public interface IBarkodDao {
    Barkod getBarkodWithBarkod(String barkodu);
}
