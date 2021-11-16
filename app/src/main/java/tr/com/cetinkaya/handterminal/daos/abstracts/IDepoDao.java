package tr.com.cetinkaya.handterminal.daos.abstracts;

import tr.com.cetinkaya.handterminal.models.Depo;

public interface IDepoDao {
    Depo getDepoById(int id);

    int updateDepo(Depo depo);

    void insertDepo(Depo depo);
}
