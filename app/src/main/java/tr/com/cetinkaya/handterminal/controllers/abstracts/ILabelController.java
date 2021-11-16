package tr.com.cetinkaya.handterminal.controllers.abstracts;

import tr.com.cetinkaya.handterminal.business.concretes.BarkodBO;
import tr.com.cetinkaya.handterminal.business.concretes.DepoBO;
import tr.com.cetinkaya.handterminal.business.concretes.StokBO;
import tr.com.cetinkaya.handterminal.business.concretes.StokSatisFiyatBO;
import tr.com.cetinkaya.handterminal.dtos.LabelDto;

public interface ILabelController {
    LabelDto getLabel(String barkod, int etiketDepo, int indirimDepo);
}
