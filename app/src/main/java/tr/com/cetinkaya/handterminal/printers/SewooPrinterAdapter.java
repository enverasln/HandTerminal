package tr.com.cetinkaya.handterminal.printers;

import android.graphics.Typeface;
import android.os.Environment;

import com.sewoo.jpos.command.CPCLConst;
import com.sewoo.jpos.printer.CPCLPrinter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import tr.com.cetinkaya.handterminal.dtos.LabelDto;

public class SewooPrinterAdapter extends IPrinterAdapter {

    private CPCLPrinter printer;
    private int paperType;

    public SewooPrinterAdapter() {
        printer = new CPCLPrinter();
        paperType = CPCLConst.LK_CPCL_LABEL;
    }


    public void printBarkodsuzKırmızıEtiket(LabelDto labelDto, int count) {

        try {
            int nLineWidth = 384;
            printer.setForm(0, 384, 406, 406, count);
            printer.setMedia(paperType);

            int stokAdiLength = labelDto.getStokAdi().length();

            String stokAdiSatir1 = "";
            String stokAdiSatir2 = "";
            if (stokAdiLength < 18) {
                stokAdiSatir1 = labelDto.getStokAdi().substring(0, stokAdiLength - 1);
            } else if (stokAdiLength < 36) {
                stokAdiSatir1 = labelDto.getStokAdi().substring(0, 18);
                stokAdiSatir2 = labelDto.getStokAdi().substring(18, stokAdiLength - 1);
            } else {
                stokAdiSatir1 = labelDto.getStokAdi().substring(0, 18);
                stokAdiSatir2 = labelDto.getStokAdi().substring(18, 36);
            }


            printer.printCPCLText(CPCLConst.LK_CPCL_0_ROTATION, 5, 1, 0, 10, stokAdiSatir1, 0);
            printer.printCPCLText(CPCLConst.LK_CPCL_0_ROTATION, 5, 1, 0, 50, stokAdiSatir2, 0);
            printer.printCPCLText(CPCLConst.LK_CPCL_0_ROTATION, 7, 1, 0, 100, labelDto.getStokKodu(), 0);

            String beden = labelDto.getBeden();
            if (beden != null && !beden.isEmpty()) {
                printer.printCPCLText(CPCLConst.LK_CPCL_0_ROTATION, 7, 0, 200, 105, "BEDEN", 0);
                printer.printAndroidFont(200, 125, Typeface.DEFAULT, true, false, beden, nLineWidth, 24);
            }
            String etiketFiyati = String.format("%.2f", labelDto.getEtiketFiyati());
            printer.printCPCLText(CPCLConst.LK_CPCL_0_ROTATION, 5, 3, 0, 135, etiketFiyati, 0);
            int tlOteleme = (int) ((etiketFiyati.length() + 0.5) * 2.2 * 8);
            printer.printAndroidFont(0 + tlOteleme, 155, Typeface.DEFAULT, false, false, "₺", nLineWidth, 50);
            if (etiketFiyati.length() <= 5) {
                printer.printLine(0, 145, 134, 210, 5);
                printer.printLine(134, 145, 0, 210, 5);
            } else {
                printer.printLine(0, 145, 164, 210, 5);
                printer.printLine(164, 145, 0, 210, 5);
            }

            String satisFiyati = String.format("%.2f", labelDto.getSatisFiyati());
            printer.printCPCLText(CPCLConst.LK_CPCL_0_ROTATION, 5, 3, 0, 205, satisFiyati, 0);
            tlOteleme = (int) ((satisFiyati.length() + 0.5) * 2.2 * 8);
            printer.printAndroidFont(0 + tlOteleme, 225, Typeface.DEFAULT, false, false, "₺", nLineWidth, 50);


            printer.printAndroidFont(200, 155, Typeface.DEFAULT, false, false, "İNDİRİM", nLineWidth, 24);
            printer.printAndroidFont(200, 180, Typeface.DEFAULT, false, false, "ORANI", nLineWidth, 24);
            String indirimOraniStr = String.format("%.0f", labelDto.getIndirimOrani());
            printer.printAndroidFont(180, 205, Typeface.DEFAULT, true, false, "%" + indirimOraniStr, nLineWidth, 50);


            if (labelDto.getYerliUretim() == 0) {
                final String yerliUretimLogo = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/MobilEtiket/yerliUretim100.png";
                printer.printBitmap(yerliUretimLogo, 0, 285);
            }

            printer.printAndroidFont(120, 280, Typeface.DEFAULT, true, false, "Fiyat Değ.Tarihi", nLineWidth, 20);
            String fiyatDegisiklikTarihi = getFormattedDate(labelDto.getFiyatDegTarihi().substring(0, 11));
            printer.printAndroidFont(120, 305, Typeface.DEFAULT, true, false, fiyatDegisiklikTarihi, nLineWidth, 20);

            printer.printAndroidFont(0, 330, Typeface.DEFAULT, true, false, "Menşei: " + labelDto.getMensei(), nLineWidth, 24);
            printer.printForm();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void printBarkodluKırmızıEtiket(LabelDto labelDto, int count) {
        try {
            int nLineWidth = 384;
            printer.setForm(0, 384, 406, 406, count);
            printer.setMedia(paperType);

            int stokAdiLength = labelDto.getStokAdi().length();

            String stokAdiSatir1 = "";
            String stokAdiSatir2 = "";
            if (stokAdiLength < 18) {
                stokAdiSatir1 = labelDto.getStokAdi().substring(0, stokAdiLength - 1);
            } else if (stokAdiLength < 36) {
                stokAdiSatir1 = labelDto.getStokAdi().substring(0, 18);
                stokAdiSatir2 = labelDto.getStokAdi().substring(18, stokAdiLength - 1);
            } else {
                stokAdiSatir1 = labelDto.getStokAdi().substring(0, 18);
                stokAdiSatir2 = labelDto.getStokAdi().substring(18, 36);
            }


            printer.printAndroidFont(5, 5, Typeface.DEFAULT, true, false, stokAdiSatir1, nLineWidth, 28);
            printer.printAndroidFont(5, 35, Typeface.DEFAULT, true, false, stokAdiSatir2, nLineWidth, 28);

            if(labelDto.getBarkod().length() > 11){
                printer.printCPCLBarcode(CPCLConst.LK_CPCL_0_ROTATION, CPCLConst.LK_CPCL_BCS_EAN13, 1, CPCLConst.LK_CPCL_BCS_1RATIO, 50, 50, 70, labelDto.getBarkod(), 0);
            } else {
                printer.printCPCLBarcode(CPCLConst.LK_CPCL_0_ROTATION, CPCLConst.LK_CPCL_BCS_128, 1, CPCLConst.LK_CPCL_BCS_1RATIO, 50, 50, 70, labelDto.getBarkod(), 0);
            }

            printer.printAndroidFont(40, 120, Typeface.DEFAULT, false, false, labelDto.getBarkod(), nLineWidth, 24);

            String beden = labelDto.getBeden();
            if (beden != null && !beden.isEmpty()) {
                printer.printCPCLText(CPCLConst.LK_CPCL_0_ROTATION, 7, 0, 210, 150, "BEDEN", 0);
                printer.printAndroidFont(200, 170, Typeface.DEFAULT, true, false, beden, nLineWidth, 24);
            }
            String etiketFiyati = String.format("%.2f", labelDto.getEtiketFiyati());
            printer.printCPCLText(CPCLConst.LK_CPCL_0_ROTATION, 5, 3, 0, 135, etiketFiyati, 0);
            int tlOteleme = (int) ((etiketFiyati.length() + 0.5) * 2.2 * 8);
            printer.printAndroidFont(0 + tlOteleme, 155, Typeface.DEFAULT, false, false, "₺", nLineWidth, 50);
            if (etiketFiyati.length() <= 5) {
                printer.printLine(0, 145, 134, 210, 5);
                printer.printLine(134, 145, 0, 210, 5);
            } else {
                printer.printLine(0, 145, 164, 210, 5);
                printer.printLine(164, 145, 0, 210, 5);
            }


            String satisFiyati = String.format("%.2f", labelDto.getSatisFiyati());
            printer.printCPCLText(CPCLConst.LK_CPCL_0_ROTATION, 5, 3, 0, 205, satisFiyati, 0);
            tlOteleme = (int) ((satisFiyati.length() + 0.5) * 2.2 * 8);
            printer.printAndroidFont(0 + tlOteleme, 225, Typeface.DEFAULT, false, false, "₺", nLineWidth, 50);

            printer.printAndroidFont(200, 195, Typeface.DEFAULT, false, false, "İNDİRİM", nLineWidth, 24);
            printer.printAndroidFont(200, 215, Typeface.DEFAULT, false, false, "ORANI", nLineWidth, 24);
            String indirimOraniStr = String.format("%.0f", labelDto.getIndirimOrani());
            printer.printAndroidFont(180, 230, Typeface.DEFAULT, true, false, "%" + indirimOraniStr, nLineWidth, 50);


            if (labelDto.getYerliUretim() == 0) {
                final String yerliUretimLogo = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/MobilEtiket/yerliUretim100.png";
                printer.printBitmap(yerliUretimLogo, 0, 285);
            }

            printer.printAndroidFont(120, 280, Typeface.DEFAULT, true, false, "Fiyat Değ.Tarihi", nLineWidth, 20);
            String fiyatDegisiklikTarihi = getFormattedDate(labelDto.getFiyatDegTarihi().substring(0, 11));
            printer.printAndroidFont(120, 305, Typeface.DEFAULT, false, false, fiyatDegisiklikTarihi, nLineWidth, 20);

            printer.printAndroidFont(0, 330, Typeface.DEFAULT, true, false, "Menşei: " + labelDto.getMensei(), nLineWidth, 24);
            printer.printForm();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printBarkodsuzBeyazEtiket(LabelDto labelDto, int count) {
        try {
            int nLineWidth = 384;
            printer.setForm(0, 384, 406, 406, count);
            printer.setMedia(paperType);

            int stokAdiLength = labelDto.getStokAdi().length();

            String stokAdiSatir1 = "";
            String stokAdiSatir2 = "";
            if (stokAdiLength < 18) {
                stokAdiSatir1 = labelDto.getStokAdi().substring(0, stokAdiLength - 1);
            } else if (stokAdiLength < 36) {
                stokAdiSatir1 = labelDto.getStokAdi().substring(0, 18);
                stokAdiSatir2 = labelDto.getStokAdi().substring(18, stokAdiLength - 1);
            } else {
                stokAdiSatir1 = labelDto.getStokAdi().substring(0, 18);
                stokAdiSatir2 = labelDto.getStokAdi().substring(18, 36);
            }

            printer.printCPCLText(CPCLConst.LK_CPCL_0_ROTATION, 5, 1, 0, 10, stokAdiSatir1, 0);
            printer.printCPCLText(CPCLConst.LK_CPCL_0_ROTATION, 5, 1, 0, 50, stokAdiSatir2, 0);
            printer.printCPCLText(CPCLConst.LK_CPCL_0_ROTATION, 7, 1, 50, 100, labelDto.getStokKodu(), 0);
            // Beden bilgisi kaldırıldı. 01.10.2021 - Mağaza yöneticilerinden gelen talep doğrultusunda
            /*
            if (!labelDto.getBeden().isEmpty()) {
                printer.printCPCLText(CPCLConst.LK_CPCL_0_ROTATION, 7, 0, 175, 150, "BEDEN", 0);
                printer.printAndroidFont(175, 170, Typeface.DEFAULT, false, false, labelDto.getBeden(), nLineWidth, 24);
            }
            */

            String satisFiyati = String.format("%.2f", labelDto.getSatisFiyati());
            printer.printCPCLText(CPCLConst.LK_CPCL_0_ROTATION, 5, 3, 15, 135, satisFiyati, 0);
            int tlOteleme = (int) ((satisFiyati.length() + 0.5) * 2.2 * 8);
            printer.printAndroidFont(15 + tlOteleme, 155, Typeface.DEFAULT, false, false, "₺", nLineWidth, 50);

            if (labelDto.getYerliUretim() == 0) {
                final String yerliUretimLogo = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/MobilEtiket/yerliUretim100.png";
                printer.printBitmap(yerliUretimLogo, 0, 220);
            }
            printer.printAndroidFont(103, 220, Typeface.DEFAULT, true, false, "Fiyat Değ.Tarihi", nLineWidth, 20);
            String fiyatDegisiklikTarihi = getFormattedDate(labelDto.getFiyatDegTarihi().substring(0, 11));
            printer.printAndroidFont(103, 240, Typeface.DEFAULT, true, false, fiyatDegisiklikTarihi, nLineWidth, 20);
            printer.printAndroidFont(0, 285, Typeface.DEFAULT, true, false, "Menşei: " + labelDto.getMensei(), nLineWidth, 24);

            printer.printForm();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void printCiftBarkodsuzBeyazEtiket(LabelDto labelDto, int count) {
        try {
            int nLineWidth = 384;
            printer.setForm(0, 200, 200, 336, count);
            printer.setMedia(paperType);

            int stokAdiLength = labelDto.getStokAdi().length();

            String stokAdiSatir1 = "";
            String stokAdiSatir2 = "";
            if (stokAdiLength < 18) {
                stokAdiSatir1 = labelDto.getStokAdi().substring(0, stokAdiLength - 1);
            } else if (stokAdiLength < 36) {
                stokAdiSatir1 = labelDto.getStokAdi().substring(0, 18);
                stokAdiSatir2 = labelDto.getStokAdi().substring(18, stokAdiLength - 1);
            } else {
                stokAdiSatir1 = labelDto.getStokAdi().substring(0, 18);
                stokAdiSatir2 = labelDto.getStokAdi().substring(18, 36);
            }

            printer.printCPCLText(CPCLConst.LK_CPCL_0_ROTATION, 5, 1, 0, 10, stokAdiSatir1, 0);
            printer.printCPCLText(CPCLConst.LK_CPCL_0_ROTATION, 5, 1, 0, 50, stokAdiSatir2, 0);
            printer.printCPCLText(CPCLConst.LK_CPCL_0_ROTATION, 7, 1, 50, 100, labelDto.getStokKodu(), 0);


            printer.printCPCLText(CPCLConst.LK_CPCL_0_ROTATION, 5, 1, 310, 10, stokAdiSatir1, 0);
            printer.printCPCLText(CPCLConst.LK_CPCL_0_ROTATION, 5, 1, 310, 50, stokAdiSatir2, 0);
            printer.printCPCLText(CPCLConst.LK_CPCL_0_ROTATION, 7, 1, 360, 100, labelDto.getStokKodu(), 0);
            // Beden bilgisi kaldırıldı. 01.10.2021 - Mağaza yöneticilerinden gelen talep doğrultusunda
            /*
            if (!labelDto.getBeden().isEmpty()) {
                printer.printCPCLText(CPCLConst.LK_CPCL_0_ROTATION, 7, 0, 175, 150, "BEDEN", 0);
                printer.printAndroidFont(175, 170, Typeface.DEFAULT, false, false, labelDto.getBeden(), nLineWidth, 24);
            }
            */

            String satisFiyati = String.format("%.2f", labelDto.getSatisFiyati());
            printer.printCPCLText(CPCLConst.LK_CPCL_0_ROTATION, 5, 3, 15, 135, satisFiyati, 0);
            printer.printCPCLText(CPCLConst.LK_CPCL_0_ROTATION, 5, 3, 325, 135, satisFiyati, 0);
            int tlOteleme = (int) ((satisFiyati.length() + 0.5) * 2.2 * 8);
            printer.printAndroidFont(15 + tlOteleme, 155, Typeface.DEFAULT, false, false, "₺", nLineWidth, 50);
            printer.printAndroidFont(325 + tlOteleme, 155, Typeface.DEFAULT, false, false, "₺", nLineWidth, 50);

            if (labelDto.getYerliUretim() == 0) {
                final String yerliUretimLogo = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/MobilEtiket/yerliUretim100.png";
                printer.printBitmap(yerliUretimLogo, 0, 220);
                printer.printBitmap(yerliUretimLogo, 310, 220);
            }
            printer.printAndroidFont(103, 220, Typeface.DEFAULT, true, false, "Fiyat Değ.Tarihi", nLineWidth, 20);
            printer.printAndroidFont(413, 220, Typeface.DEFAULT, true, false, "Fiyat Değ.Tarihi", nLineWidth, 20);
            String fiyatDegisiklikTarihi = getFormattedDate(labelDto.getFiyatDegTarihi().substring(0, 11));
            printer.printAndroidFont(103, 240, Typeface.DEFAULT, true, false, fiyatDegisiklikTarihi, nLineWidth, 20);
            printer.printAndroidFont(413, 240, Typeface.DEFAULT, true, false, fiyatDegisiklikTarihi, nLineWidth, 20);
            printer.printAndroidFont(0, 285, Typeface.DEFAULT, true, false, "Menşei: " + labelDto.getMensei(), nLineWidth, 24);
            printer.printAndroidFont(310, 285, Typeface.DEFAULT, true, false, "Menşei: " + labelDto.getMensei(), nLineWidth, 24);

            printer.printForm();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void printBarkodluBeyazEtiket(LabelDto labelDto, int count) {
        try {
            int nLineWidth = 384;
            printer.setForm(0, 384, 406, 406, count);
            printer.setMedia(paperType);

            int stokAdiLength = labelDto.getStokAdi().length();

            String stokAdiSatir1 = "";
            String stokAdiSatir2 = "";
            if (stokAdiLength < 18) {
                stokAdiSatir1 = labelDto.getStokAdi().substring(0, stokAdiLength - 1);
            } else if (stokAdiLength < 36) {
                stokAdiSatir1 = labelDto.getStokAdi().substring(0, 18);
                stokAdiSatir2 = labelDto.getStokAdi().substring(18, stokAdiLength - 1);
            } else {
                stokAdiSatir1 = labelDto.getStokAdi().substring(0, 18);
                stokAdiSatir2 = labelDto.getStokAdi().substring(18, 36);
            }

            printer.printCPCLText(CPCLConst.LK_CPCL_0_ROTATION, 5, 1, 0, 10, stokAdiSatir1, 0);
            printer.printCPCLText(CPCLConst.LK_CPCL_0_ROTATION, 5, 1, 0, 50, stokAdiSatir2, 0);
            if(labelDto.getBarkod().length() > 11){
                printer.printCPCLBarcode(CPCLConst.LK_CPCL_0_ROTATION, CPCLConst.LK_CPCL_BCS_EAN13, 1, CPCLConst.LK_CPCL_BCS_1RATIO, 50, 30, 100, labelDto.getBarkod(), 0);
            } else {
                printer.printCPCLBarcode(CPCLConst.LK_CPCL_0_ROTATION, CPCLConst.LK_CPCL_BCS_128, 1, CPCLConst.LK_CPCL_BCS_1RATIO, 50, 30, 100, labelDto.getBarkod(), 0);
            }
            printer.printAndroidFont(40, 150, Typeface.DEFAULT, false, false, labelDto.getBarkod(), nLineWidth, 24);

            String beden = labelDto.getBeden();
            if (beden != null && !beden.isEmpty()) {
                printer.printCPCLText(CPCLConst.LK_CPCL_0_ROTATION, 7, 0, 175, 175, "BEDEN", 0);

                printer.printAndroidFont(175, 195, Typeface.DEFAULT, false, false, beden, nLineWidth, 24);
            }

            String satiFiyati = String.format("%.2f", labelDto.getSatisFiyati());
            printer.printCPCLText(CPCLConst.LK_CPCL_0_ROTATION, 5, 3, 15, 160, satiFiyati, 0);
            int tlOteleme = (int) ((satiFiyati.length() + 0.5) * 2.2 * 8);
            printer.printAndroidFont(15 + tlOteleme, 180, Typeface.DEFAULT, false, false, "₺", nLineWidth, 50);

            if (labelDto.getYerliUretim() == 0) {
                final String yerliUretimLogo = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/MobiltegDB/yerliUretim100.png";
                try {
                    printer.printBitmap(yerliUretimLogo, 0, 240);
                    printer.setMedia(paperType);
                } catch (Exception e) {

                }

            }
            printer.printAndroidFont(103, 240, Typeface.DEFAULT, true, false, "Fiyat Değ.Tarihi", nLineWidth, 20);
            String fiyatDegisiklikTarihi = getFormattedDate(labelDto.getFiyatDegTarihi().substring(0, 11));
            printer.printAndroidFont(103, 260, Typeface.DEFAULT, true, false, fiyatDegisiklikTarihi, nLineWidth, 20);
            printer.printAndroidFont(0, 285, Typeface.DEFAULT, true, false, "Menşei: " + labelDto.getMensei(), nLineWidth, 24);

            printer.printForm();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void printCiftBarkodluBeyazEtiket(LabelDto labelDto, int count) {
        try {
            int nLineWidth = 384;
            printer.setForm(0, 384, 406, 406, count);
            printer.setMedia(paperType);

            int stokAdiLength = labelDto.getStokAdi().length();

            String stokAdiSatir1 = "";
            String stokAdiSatir2 = "";
            if (stokAdiLength < 18) {
                stokAdiSatir1 = labelDto.getStokAdi().substring(0, stokAdiLength - 1);
            } else if (stokAdiLength < 18) {
                stokAdiSatir1 = labelDto.getStokAdi().substring(0, 18);
                stokAdiSatir2 = labelDto.getStokAdi().substring(18, stokAdiLength - 1);
            } else {
                stokAdiSatir1 = labelDto.getStokAdi().substring(0, 18);
                stokAdiSatir2 = labelDto.getStokAdi().substring(18, 36);
            }

            printer.printCPCLText(CPCLConst.LK_CPCL_0_ROTATION, 5, 1, 0, 10, stokAdiSatir1, 0);
            printer.printCPCLText(CPCLConst.LK_CPCL_0_ROTATION, 5, 1, 0, 50, stokAdiSatir2, 0);
            if(labelDto.getBarkod().length() > 11){
                printer.printCPCLBarcode(CPCLConst.LK_CPCL_0_ROTATION, CPCLConst.LK_CPCL_BCS_EAN13, 1, CPCLConst.LK_CPCL_BCS_1RATIO, 50, 30, 100, labelDto.getBarkod(), 0);
                printer.printCPCLBarcode(CPCLConst.LK_CPCL_0_ROTATION, CPCLConst.LK_CPCL_BCS_EAN13, 1, CPCLConst.LK_CPCL_BCS_1RATIO, 50, 340, 100, labelDto.getBarkod(), 0);
            } else {
                printer.printCPCLBarcode(CPCLConst.LK_CPCL_0_ROTATION, CPCLConst.LK_CPCL_BCS_128, 1, CPCLConst.LK_CPCL_BCS_1RATIO, 50, 30, 100, labelDto.getBarkod(), 0);
                printer.printCPCLBarcode(CPCLConst.LK_CPCL_0_ROTATION, CPCLConst.LK_CPCL_BCS_128, 1, CPCLConst.LK_CPCL_BCS_1RATIO, 50, 340, 100, labelDto.getBarkod(), 0);
            }

            printer.printAndroidFont(40, 150, Typeface.DEFAULT, false, false, labelDto.getBarkod(), nLineWidth, 24);
            printer.printCPCLText(CPCLConst.LK_CPCL_0_ROTATION, 5, 1, 310, 10, stokAdiSatir1, 0);
            printer.printCPCLText(CPCLConst.LK_CPCL_0_ROTATION, 5, 1, 310, 50, stokAdiSatir2, 0);

            printer.printAndroidFont(350, 150, Typeface.DEFAULT, false, false, labelDto.getBarkod(), nLineWidth, 24);
            String beden = labelDto.getBeden();
            if (beden != null && !beden.isEmpty()) {

                printer.printCPCLText(CPCLConst.LK_CPCL_0_ROTATION, 7, 0, 175, 175, "BEDEN", 0);

                printer.printAndroidFont(175, 195, Typeface.DEFAULT, false, false, labelDto.getBeden(), nLineWidth, 24);

                printer.printCPCLText(CPCLConst.LK_CPCL_0_ROTATION, 7, 0, 485, 175, "BEDEN", 0);

                printer.printAndroidFont(485, 195, Typeface.DEFAULT, false, false, labelDto.getBeden(), nLineWidth, 24);
            }

            String satiFiyati = String.format("%.2f", labelDto.getSatisFiyati());
            printer.printCPCLText(CPCLConst.LK_CPCL_0_ROTATION, 5, 3, 15, 160, satiFiyati, 0);
            printer.printCPCLText(CPCLConst.LK_CPCL_0_ROTATION, 5, 3, 325, 160, satiFiyati, 0);
            int tlOteleme = (int) ((satiFiyati.length() + 0.5) * 2.2 * 8);
            printer.printAndroidFont(15 + tlOteleme, 180, Typeface.DEFAULT, false, false, "₺", nLineWidth, 50);
            printer.printAndroidFont(325 + tlOteleme, 180, Typeface.DEFAULT, false, false, "₺", nLineWidth, 50);
            if (labelDto.getYerliUretim() == 0) {
                final String yerliUretimLogo = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/MobiltegDB/yerliUretim100.png";
                try {
                    printer.printBitmap(yerliUretimLogo, 0, 240);
                    printer.printBitmap(yerliUretimLogo, 310, 240);
                } catch (Exception e) {

                }

            }
            printer.printAndroidFont(103, 240, Typeface.DEFAULT, true, false, "Fiyat Değ.Tarihi", nLineWidth, 20);
            printer.printAndroidFont(413, 240, Typeface.DEFAULT, true, false, "Fiyat Değ.Tarihi", nLineWidth, 20);
            String fiyatDegisiklikTarihi = getFormattedDate(labelDto.getFiyatDegTarihi().substring(0, 11));
            printer.printAndroidFont(103, 260, Typeface.DEFAULT, true, false, fiyatDegisiklikTarihi, nLineWidth, 20);
            printer.printAndroidFont(413, 260, Typeface.DEFAULT, true, false, fiyatDegisiklikTarihi, nLineWidth, 20);
            printer.printAndroidFont(0, 285, Typeface.DEFAULT, true, false, "Menşei: " + labelDto.getMensei(), nLineWidth, 24);
            printer.printAndroidFont(310, 285, Typeface.DEFAULT, true, false, "Menşei: " + labelDto.getMensei(), nLineWidth, 24);

            printer.printForm();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void printBarkodluTaksitliEtiket(LabelDto labelDto, int count) {
        try {
            int nLineWidth = 384;
            printer.setForm(0, 384, 406, 304, count);
            printer.setMedia(paperType);

            printer.printAndroidFont(55, 45, Typeface.SANS_SERIF, false, false, labelDto.getBarkod(), nLineWidth, 20);
            if(labelDto.getBarkod().length() > 11){
                printer.printCPCLBarcode(CPCLConst.LK_CPCL_90_ROTATION,
                        CPCLConst.LK_CPCL_BCS_EAN13,
                        1, CPCLConst.LK_CPCL_BCS_1RATIO,
                        50, 0, 240, labelDto.getBarkod(), 0);
            } else {
                printer.printCPCLBarcode(CPCLConst.LK_CPCL_90_ROTATION,
                        CPCLConst.LK_CPCL_BCS_128,
                        1, CPCLConst.LK_CPCL_BCS_1RATIO,
                        50, 0, 240, labelDto.getBarkod(), 0);
            }
            int stokAdiLength = labelDto.getStokAdi().length();

            String stokAdiSatir1 = " ";
            String stokAdiSatir2 = " ";
            if (stokAdiLength < 24) {
                stokAdiSatir1 = labelDto.getStokAdi().substring(0, stokAdiLength - 1);
            } else if (stokAdiLength < 48) {
                stokAdiSatir1 = labelDto.getStokAdi().substring(0, 24);
                stokAdiSatir2 = labelDto.getStokAdi().substring(24, stokAdiLength - 1);
            } else {
                stokAdiSatir1 = labelDto.getStokAdi().substring(0, 24);
                stokAdiSatir2 = labelDto.getStokAdi().substring(24, 48);
            }


            String satisFiyatiStr = String.format("%.2f", labelDto.getSatisFiyati());
            String taksitliFiyatStr = String.format("%.2f", labelDto.getTaksitFiyati());

            printer.printAndroidFont(5, 0, Typeface.SANS_SERIF, false, false, stokAdiSatir1, nLineWidth, 20);
            printer.printAndroidFont(5, 20, Typeface.SANS_SERIF, false, false, stokAdiSatir2, nLineWidth, 20);

            printer.printAndroidFont(55, 70, Typeface.SANS_SERIF, true, false, "Taksitli Fiyat", nLineWidth, 28);
            printer.printAndroidFont(55, 100, Typeface.SANS_SERIF, true, false, taksitliFiyatStr, nLineWidth, 28);
            int tlOteleme = (int) ((taksitliFiyatStr.length() + 0.5) * 1.6 * 8);
            printer.printAndroidFont(55 + tlOteleme, 100, Typeface.DEFAULT, true, false, "₺", nLineWidth, 28);


            printer.printAndroidFont(55, 140, Typeface.SANS_SERIF, true, false, "Peşin Fiyat", nLineWidth, 38);
            printer.printAndroidFont(55, 190, Typeface.SANS_SERIF, true, false, satisFiyatiStr, nLineWidth, 38);
            tlOteleme = (int) ((satisFiyatiStr.length() + 0.5) * 2.2 * 8);
            printer.printAndroidFont(55 + tlOteleme, 190, Typeface.DEFAULT, true, false, "₺", nLineWidth, 38);

            if (labelDto.getYerliUretim() == 0) {
                final String yerliUretimLogo = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/MobiltegDB/yerliUretim100.png";
                try {
                    printer.printBitmap(yerliUretimLogo, 40, 235);
                } catch (Exception e) {

                }
            }

            printer.printAndroidFont(0, 280, Typeface.DEFAULT, true, false, "Fiyat Değ.Tarihi:", nLineWidth, 18);
            String fiyatDegisiklikTarihi = getFormattedDate(labelDto.getFiyatDegTarihi().substring(0, 11));
            printer.printAndroidFont(135, 280, Typeface.DEFAULT, true, false, fiyatDegisiklikTarihi, nLineWidth, 18);
            printer.printAndroidFont(150, 235, Typeface.DEFAULT, true, false, "Menşei: ", nLineWidth, 18);
            printer.printAndroidFont(150, 255, Typeface.DEFAULT, true, false, labelDto.getMensei(), nLineWidth, 18);


            printer.printForm();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void printBarkodsuzTaksitliEtiket(LabelDto labelDto, int count) {
        try {
            int nLineWidth = 384;
            printer.setForm(0, 384, 406, 304, count);
            printer.setMedia(paperType);
            int stokAdiLength = labelDto.getStokAdi().length();

            String stokAdiSatir1 = " ";
            String stokAdiSatir2 = " ";
            if (stokAdiLength < 24) {
                stokAdiSatir1 = labelDto.getStokAdi().substring(0, stokAdiLength - 1);
            } else if (stokAdiLength < 48) {
                stokAdiSatir1 = labelDto.getStokAdi().substring(0, 24);
                stokAdiSatir2 = labelDto.getStokAdi().substring(24, stokAdiLength - 1);
            } else {
                stokAdiSatir1 = labelDto.getStokAdi().substring(0, 24);
                stokAdiSatir2 = labelDto.getStokAdi().substring(24, 48);
            }


            String satisFiyatiStr = String.format("%.2f", labelDto.getSatisFiyati());
            String taksitliFiyatStr = String.format("%.2f", labelDto.getTaksitFiyati());

            printer.printAndroidFont(5, 0, Typeface.SANS_SERIF, false, false, stokAdiSatir1, nLineWidth, 20);
            printer.printAndroidFont(5, 20, Typeface.SANS_SERIF, false, false, stokAdiSatir2, nLineWidth, 20);

            printer.printAndroidFont(55, 70, Typeface.SANS_SERIF, true, false, "Taksitli Fiyat", nLineWidth, 28);
            printer.printAndroidFont(55, 100, Typeface.SANS_SERIF, true, false, taksitliFiyatStr, nLineWidth, 28);
            int tlOteleme = (int) ((taksitliFiyatStr.length() + 0.5) * 1.6 * 8);
            printer.printAndroidFont(55 + tlOteleme, 100, Typeface.DEFAULT, true, false, "₺", nLineWidth, 28);


            printer.printAndroidFont(55, 140, Typeface.SANS_SERIF, true, false, "Peşin Fiyat", nLineWidth, 38);
            printer.printAndroidFont(55, 190, Typeface.SANS_SERIF, true, false, satisFiyatiStr, nLineWidth, 38);
            tlOteleme = (int) ((satisFiyatiStr.length() + 0.5) * 2.2 * 8);
            printer.printAndroidFont(55 + tlOteleme, 190, Typeface.DEFAULT, true, false, "₺", nLineWidth, 38);

            if (labelDto.getYerliUretim() == 0) {
                final String yerliUretimLogo = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/MobiltegDB/yerliUretim100.png";
                try {
                    printer.printBitmap(yerliUretimLogo, 40, 235);
                } catch (Exception e) {

                }
            }

            printer.printAndroidFont(0, 280, Typeface.DEFAULT, true, false, "Fiyat Değ.Tarihi:", nLineWidth, 18);
            String fiyatDegisiklikTarihi = getFormattedDate(labelDto.getFiyatDegTarihi().substring(0, 11));
            printer.printAndroidFont(135, 280, Typeface.DEFAULT, true, false, fiyatDegisiklikTarihi, nLineWidth, 18);
            printer.printAndroidFont(150, 235, Typeface.DEFAULT, true, false, "Menşei: ", nLineWidth, 18);
            printer.printAndroidFont(150, 255, Typeface.DEFAULT, true, false, labelDto.getMensei(), nLineWidth, 18);


            printer.printForm();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void printCiftTaksitliEtiket(LabelDto labelDto, int count) {
        try {
            int nLineWidth = 384;
            printer.setForm(0, 384, 304, 304, count);
            printer.setMedia(paperType);


            int stokAdiLength = labelDto.getStokAdi().length();

            String stokAdiSatir1 = " ";
            String stokAdiSatir2 = " ";
            if (stokAdiLength < 24) {
                stokAdiSatir1 = labelDto.getStokAdi().substring(0, stokAdiLength - 1);
            } else if (stokAdiLength < 48) {
                stokAdiSatir1 = labelDto.getStokAdi().substring(0, 24);
                stokAdiSatir2 = labelDto.getStokAdi().substring(24, stokAdiLength - 1);
            } else {
                stokAdiSatir1 = labelDto.getStokAdi().substring(0, 24);
                stokAdiSatir2 = labelDto.getStokAdi().substring(24, 48);
            }


            String satisFiyatiStr = String.format("%.2f", labelDto.getSatisFiyati());
            String taksitliFiyatStr = String.format("%.2f", labelDto.getTaksitFiyati());


            printer.printAndroidFont(0, 10, Typeface.SANS_SERIF, false, false, stokAdiSatir1, nLineWidth, 20);
            printer.printAndroidFont(0, 40, Typeface.SANS_SERIF, false, false, stokAdiSatir2, nLineWidth, 20);

            printer.printAndroidFont(50, 70, Typeface.SANS_SERIF, true, false, "Taksitli Fiyat", nLineWidth, 28);
            printer.printAndroidFont(50, 100, Typeface.SANS_SERIF, true, false, taksitliFiyatStr, nLineWidth, 28);


            printer.printAndroidFont(310, 10, Typeface.SANS_SERIF, false, false, stokAdiSatir1, nLineWidth, 20);
            printer.printAndroidFont(310, 40, Typeface.SANS_SERIF, false, false, stokAdiSatir2, nLineWidth, 20);

            printer.printAndroidFont(360, 70, Typeface.SANS_SERIF, true, false, "Taksitli Fiyat", nLineWidth, 28);
            printer.printAndroidFont(360, 100, Typeface.SANS_SERIF, true, false, taksitliFiyatStr, nLineWidth, 28);

            int tlOteleme = (int) ((taksitliFiyatStr.length() + 0.5) * 1.6 * 8);
            printer.printAndroidFont(50 + tlOteleme, 100, Typeface.DEFAULT, true, false, "₺", nLineWidth, 28);

            printer.printAndroidFont(360 + tlOteleme, 100, Typeface.DEFAULT, true, false, "₺", nLineWidth, 28);


            printer.printAndroidFont(50, 140, Typeface.SANS_SERIF, true, false, "Peşin Fiyat", nLineWidth, 38);
            printer.printAndroidFont(50, 190, Typeface.SANS_SERIF, true, false, satisFiyatiStr, nLineWidth, 38);

            printer.printAndroidFont(360, 140, Typeface.SANS_SERIF, true, false, "Peşin Fiyat", nLineWidth, 38);
            printer.printAndroidFont(360, 190, Typeface.SANS_SERIF, true, false, satisFiyatiStr, nLineWidth, 38);
            tlOteleme = (int) ((satisFiyatiStr.length() + 0.5) * 2.2 * 8);
            printer.printAndroidFont(50 + tlOteleme, 190, Typeface.DEFAULT, true, false, "₺", nLineWidth, 38);

            printer.printAndroidFont(360 + tlOteleme, 190, Typeface.DEFAULT, true, false, "₺", nLineWidth, 38);

            if (labelDto.getYerliUretim() == 0) {
                final String yerliUretimLogo = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/MobiltegDB/yerliUretim100.png";
                try {
                    printer.printBitmap(yerliUretimLogo, 0, 235);
                    printer.printBitmap(yerliUretimLogo, 310, 235);
                } catch (Exception e) {

                }
            }

            printer.printAndroidFont(0, 280, Typeface.DEFAULT, true, false, "Fiyat Değ.Tarihi:", nLineWidth, 18);
            printer.printAndroidFont(310, 280, Typeface.DEFAULT, true, false, "Fiyat Değ.Tarihi:", nLineWidth, 18);
            String fiyatDegisiklikTarihi = getFormattedDate(labelDto.getFiyatDegTarihi().substring(0, 11));
            printer.printAndroidFont(135, 280, Typeface.DEFAULT, true, false, fiyatDegisiklikTarihi, nLineWidth, 18);
            printer.printAndroidFont(135, 235, Typeface.DEFAULT, true, false, "Menşei: ", nLineWidth, 18);
            printer.printAndroidFont(135, 255, Typeface.DEFAULT, true, false, labelDto.getMensei(), nLineWidth, 18);
            printer.printAndroidFont(445, 280, Typeface.DEFAULT, true, false, fiyatDegisiklikTarihi, nLineWidth, 18);
            printer.printAndroidFont(445, 235, Typeface.DEFAULT, true, false, "Menşei: ", nLineWidth, 18);
            printer.printAndroidFont(445, 255, Typeface.DEFAULT, true, false, labelDto.getMensei(), nLineWidth, 18);


            printer.printForm();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void printIndirimsizRafEtiket(LabelDto labelDto, int count) {

        try {
            int nLineWidth = 632;
            printer.setForm(0, 632, 304, 304, count);
            printer.setMedia(paperType);

            // Stok Adı
            printer.printAndroidFont(Typeface.SANS_SERIF, true, labelDto.getStokAdi(), nLineWidth, 26, 40, CPCLConst.LK_CPCL_LEFT);

            // Yerli üretim logosu
            if (labelDto.getYerliUretim() == 0) {
                final String yerliUretimLogo = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/MobiltegDB/yerliUretim120.png";
                try {
                    printer.printBitmap(yerliUretimLogo, 380, 70);
                } catch (Exception e) {
                }
            }


            // barkod
            printer.printCPCLBarcode(CPCLConst.LK_CPCL_0_ROTATION, CPCLConst.LK_CPCL_BCS_EAN13, 1,
                    CPCLConst.LK_CPCL_BCS_0RATIO, 40, 380, 160, labelDto.getBarkod(), 0);
            printer.printAndroidFont(380, 200, Typeface.SANS_SERIF, true, false, labelDto.getBarkod(), nLineWidth, 20);

            // Reyon kodu
            printer.printAndroidFont(540, 202, Typeface.SANS_SERIF, false, false, labelDto.getReyon(), nLineWidth, 16);

            // Satış fiyati label
            printer.printAndroidFont(Typeface.SANS_SERIF, true, "SATIŞ", nLineWidth, 20, 100, CPCLConst.LK_CPCL_LEFT);
            printer.printAndroidFont(Typeface.SANS_SERIF, true, "FİYATI", nLineWidth, 20, 130, CPCLConst.LK_CPCL_LEFT);
            // Satış fiyatı tutar
            String satisFiyatiStr = String.format("%.2f", labelDto.getSatisFiyati());
            int tlOteleme = (int) ((satisFiyatiStr.length() + 0.5) * 2.8 * 8);
            printer.printAndroidFont(100, 100, Typeface.SANS_SERIF, true, satisFiyatiStr, nLineWidth, 50);
            printer.printAndroidFont(100 + tlOteleme, 120, Typeface.SANS_SERIF, false, false, "₺", nLineWidth, 30);

            // Ayıraç
            printer.printAndroidFont(0, 150, Typeface.SANS_SERIF, false, false,
                    "------------------------------------------------------------", nLineWidth, 20);

            // Birim Fiyat
            printer.printAndroidFont(0, 170, Typeface.SANS_SERIF, false, true, "BİRİM", nLineWidth, 20);
            printer.printAndroidFont(0, 200, Typeface.SANS_SERIF, false, true, "FİYATI", nLineWidth, 20);
            printer.printAndroidFont(100, 175, Typeface.SANS_SERIF, false, true,
                    String.format("%.2f TL/%s", labelDto.getBirimFiyati(), labelDto.getBirimAdi()), nLineWidth, 40);
            // Fiyat değişiklik tarihi
            printer.printAndroidFont(0, 235, Typeface.SANS_SERIF, false, false, "Fiyat Değişikliği Tarihi:", nLineWidth, 18);
            String fiyatDegisiklikTarihi = getFormattedDate(labelDto.getFiyatDegTarihi().substring(0, 11));
            printer.printAndroidFont(180, 235, Typeface.SANS_SERIF, false, false, fiyatDegisiklikTarihi, nLineWidth, 18);

            // Menşei
            printer.printAndroidFont(380, 235, Typeface.SANS_SERIF, false, false, "Menşei:", nLineWidth, 18);

            printer.printAndroidFont(450, 235, Typeface.SANS_SERIF, false, false, labelDto.getMensei(), nLineWidth, 18);


            printer.printForm();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void printIndirimliRafEtiket(LabelDto labelDto, int count) {
        try {
            int nLineWidth = 632;
            printer.setForm(0, 632, 304, 304, count);
            printer.setMedia(paperType);

            // Stok Adı
            printer.printAndroidFont(Typeface.SANS_SERIF, true, labelDto.getStokAdi(), nLineWidth, 26, 40, CPCLConst.LK_CPCL_LEFT);

            // Yerli üretim logosu
            if (labelDto.getEtiketFiyati() == 0) {
                final String yerliUretimLogo = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/MobiltegDB/yerliUretim150.jpg";
                try {
                    printer.printBitmap(yerliUretimLogo, 405, 85);
                } catch (Exception e) {
                }
            }

            //printer.setCPCLBarcode(0, 2,0);
            // barkod
            printer.printCPCLBarcode(CPCLConst.LK_CPCL_0_ROTATION, CPCLConst.LK_CPCL_BCS_EAN13, 1,
                    CPCLConst.LK_CPCL_BCS_1RATIO, 40, 385, 160, labelDto.getBarkod(), 0);
            printer.printAndroidFont(385, 200, Typeface.SANS_SERIF, true, false, labelDto.getBarkod(), nLineWidth, 20);


            // Reyon kodu
            printer.printBox(535, 202, 575, 222, 1);
            printer.printAndroidFont(540, 203, Typeface.SANS_SERIF, false, false, labelDto.getReyon(), nLineWidth, 16);

            // Sayış fiyati label
            printer.printAndroidFont(Typeface.SANS_SERIF, true, "SATIŞ", nLineWidth, 20, 100, CPCLConst.LK_CPCL_LEFT);
            printer.printAndroidFont(Typeface.SANS_SERIF, true, "FİYATI", nLineWidth, 20, 130, CPCLConst.LK_CPCL_LEFT);
            // Satış fiyatı tutar
            String etiketFiyati = String.format("%.2f", labelDto.getEtiketFiyati());
            printer.printAndroidFont(65, 115, Typeface.SANS_SERIF, true, etiketFiyati, nLineWidth, 30);
            printer.printLine(55, 110, 150, 155, 4);
            printer.printLine(150, 110, 55, 155, 4);
            int tlOteleme = (int) ((etiketFiyati.length() + 0.5) * 1.6 * 8);
            printer.printAndroidFont(65 + tlOteleme, 115, Typeface.SANS_SERIF, false, false, "₺", nLineWidth, 30);

            // İndirim fiyati label
            printer.printAndroidFont(185, 100, Typeface.SANS_SERIF, true, "İND.", nLineWidth, 20);
            printer.printAndroidFont(185, 130, Typeface.SANS_SERIF, true, "FİYATI", nLineWidth, 20);
            // İndirim fiyatı tutar
            String satisFiyati = String.format("%.2f", labelDto.getSatisFiyati());
            printer.printAndroidFont(245, 95, Typeface.SANS_SERIF, true, satisFiyati, nLineWidth, 45);
            tlOteleme = (int) ((etiketFiyati.length() + 0.5) * 2.5 * 8);
            printer.printAndroidFont(245 + tlOteleme, 105, Typeface.SANS_SERIF, false, false, "₺", nLineWidth, 30);

            // Ayıraç
            printer.printAndroidFont(0, 150, Typeface.SANS_SERIF, false, false,
                    "------------------------------------------------------------", nLineWidth, 20);

            // Birim Fiyat
            printer.printAndroidFont(0, 170, Typeface.SANS_SERIF, false, true, "BİRİM", nLineWidth, 20);
            printer.printAndroidFont(0, 200, Typeface.SANS_SERIF, false, true, "FİYATI", nLineWidth, 20);
            printer.printAndroidFont(65, 175, Typeface.SANS_SERIF, false, true,
                    String.format("%.2f TL/%s", labelDto.getBirimFiyati(), labelDto.getBirimAdi()), nLineWidth, 30);


            // Fiyat değişiklik tarihi
            printer.printAndroidFont(0, 235, Typeface.SANS_SERIF, false, false, "Fiyat Değişikliği Tarihi:", nLineWidth, 18);
            String fiyatDegisiklikTarihi = getFormattedDate(labelDto.getFiyatDegTarihi().substring(0, 11));
            printer.printAndroidFont(180, 235, Typeface.SANS_SERIF, false, false, fiyatDegisiklikTarihi, nLineWidth, 18);

            // Menşei
            printer.printAndroidFont(380, 235, Typeface.SANS_SERIF, false, false, "Menşei:", nLineWidth, 18);
            printer.printAndroidFont(450, 235, Typeface.SANS_SERIF, false, false, labelDto.getMensei(), nLineWidth, 18);
            printer.printForm();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public String getFormattedDate(String date) {
        String formattedDate = "";
        formattedDate = date.substring(8, 10) + "-" + date.substring(5, 7) + "-" + date.substring(0, 4);
        return formattedDate;
    }
}
