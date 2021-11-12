package tr.com.cetinkaya.handterminal.business.abstracts;

import tr.com.cetinkaya.handterminal.models.Depo;

public interface IDepoBO {
    Depo getDepoById(int id);

    int updateDepo(Depo depo);

    void insertDepo(Depo depo);
}
