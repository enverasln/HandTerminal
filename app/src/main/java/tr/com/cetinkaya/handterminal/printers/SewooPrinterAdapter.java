package tr.com.cetinkaya.handterminal.printers;

import android.graphics.Typeface;
import android.os.Environment;

import com.sewoo.jpos.command.CPCLConst;
import com.sewoo.jpos.printer.CPCLPrinter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import tr.com.cetinkaya.handterminal.dtos.LabelDto;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class SewooPrinterAdapter extends IPrinterAdapter {
    private static final DecimalFormat decfor = new DecimalFormat("0.00");
    private CPCLPrinter printer;
    private int paperType;

    public SewooPrinterAdapter() {
        printer = new CPCLPrinter();
        paperType = CPCLConst.LK_CPCL_LABEL;
    }


    public void printBarkodsuzKirmiziEtiket(LabelDto labelDto, int count) {

        try {
            int nLineWidth = 384;
            printer.setForm(0, 384, 416, 416, count);
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
            printer.printAndroidFont(0, 355, Typeface.DEFAULT, true, false, "KDV DAHİLDİR.", nLineWidth, 24);
            printer.printForm();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void printBarkodluKirmiziEtiket(LabelDto labelDto, int count) {
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
            printer.printAndroidFont(0, 355, Typeface.DEFAULT, true, false, "KDV DAHİLDİR.", nLineWidth, 24);
            printer.printForm();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printDikeyKirmiziBarkodluTaksitliEtiket(LabelDto labelDto, int count) {
        try {
            int nLineWidth = 400;
            printer.setForm(0, 400, 512, 512, count);
            printer.setMedia(paperType);

            // printer.printAndroidFont(55, 45, Typeface.SANS_SERIF, false, false, labelDto.getBarkod(), nLineWidth, 20);
            printer.printCPCLText(CPCLConst.LK_CPCL_90_ROTATION, CPCLConst.LK_CPCL_FONT_7, 0, 55,245, labelDto.getBarkod(), 1);
            if(labelDto.getBarkod().length() > 11){
                printer.printCPCLBarcode(CPCLConst.LK_CPCL_90_ROTATION,
                        CPCLConst.LK_CPCL_BCS_EAN13,
                        1, CPCLConst.LK_CPCL_BCS_0RATIO,
                        50, 0, 260, labelDto.getBarkod(), 0);
            } else {
                printer.printCPCLBarcode(CPCLConst.LK_CPCL_90_ROTATION,
                        CPCLConst.LK_CPCL_BCS_128,
                        1, CPCLConst.LK_CPCL_BCS_0RATIO,
                        50, 0, 260, labelDto.getBarkod(), 0);
            }
            int stokAdiLength = labelDto.getStokAdi().length();

            String stokAdiSatir1 = " ";
            String stokAdiSatir2 = " ";
            String stokAdiSatir3 = " ";
            if (stokAdiLength <= 25) {
                stokAdiSatir1 = labelDto.getStokAdi().substring(0, stokAdiLength - 1).trim();
            } else if (stokAdiLength <= 50) {
                stokAdiSatir1 = labelDto.getStokAdi().substring(0, 25).trim();
                stokAdiSatir2 = labelDto.getStokAdi().substring(25, stokAdiLength - 1).trim();
            } else if (stokAdiLength < 75) {
                stokAdiSatir1 = labelDto.getStokAdi().substring(0, 25).trim();
                stokAdiSatir2 = labelDto.getStokAdi().substring(25, 50).trim();
                stokAdiSatir3 = labelDto.getStokAdi().substring(50, stokAdiLength - 1).trim();
            } else {
                stokAdiSatir1 = labelDto.getStokAdi().substring(0, 25).trim();
                stokAdiSatir2 = labelDto.getStokAdi().substring(25, 50).trim();
                stokAdiSatir3 = labelDto.getStokAdi().substring(50, 75).trim();
            }

            String satisFiyatiStr = String.format("%.2f₺", labelDto.getSatisFiyati());
            String taksitliFiyatStr = String.format("%.2f", labelDto.getTaksitFiyati());
            String etiketFiyatStr = String.format("%.2f", labelDto.getEtiketFiyati());

            if(!stokAdiSatir1.isEmpty())
                printer.printAndroidFont(5, 0, Typeface.SANS_SERIF, true, false, stokAdiSatir1, nLineWidth, 24);

            if(!stokAdiSatir2.isEmpty())
                printer.printAndroidFont(5, 20, Typeface.SANS_SERIF, true, false, stokAdiSatir2, nLineWidth, 24);

            if(!stokAdiSatir3.isEmpty())
                printer.printAndroidFont(5, 40, Typeface.SANS_SERIF, true, false, stokAdiSatir3, nLineWidth, 24);

            printer.printAndroidFont(80, 85, Typeface.SANS_SERIF, true, false, "Satış Fiyatı", nLineWidth, 28);
            printer.printAndroidFont(80, 112, Typeface.SANS_SERIF, true, false, etiketFiyatStr, nLineWidth, 28);
            int tlOteleme = (int) ((etiketFiyatStr.length() + 0.5) * 1.6 * 8);
            printer.printAndroidFont(80 + tlOteleme, 112, Typeface.DEFAULT, true, false, "₺", nLineWidth, 28);

            if (etiketFiyatStr.length() <= 5) {
                printer.printLine(80, 95, 150, 120, 5);
                printer.printLine(80, 120, 150, 95, 5);
            } else {
                printer.printLine(80, 95, 170, 120, 5);
                printer.printLine(80, 120, 170, 95, 5);
            }

            printer.printAndroidFont(80, 150, Typeface.SANS_SERIF, true, false, "İnd. Satış Fiyat", nLineWidth, 28);
            printer.printAndroidFont(80, 180, Typeface.SANS_SERIF, true, false, satisFiyatiStr, nLineWidth, 40);
            tlOteleme = (int) ((satisFiyatiStr.length() + 0.5) * 2.2 * 8);

            String beden = labelDto.getBeden();
            if (beden != null && !beden.isEmpty()) {
                printer.printAndroidFont(5, 275, Typeface.DEFAULT, false, false, "BEDEN", nLineWidth, 20);
                printer.printAndroidFont(5, 295, Typeface.DEFAULT, true, false, beden, nLineWidth, 24);
            }

            printer.printAndroidFont(90, 275, Typeface.DEFAULT, false, false, "İNDİRİM", nLineWidth, 20);
            printer.printAndroidFont(90, 295, Typeface.DEFAULT, false, false, "ORANI", nLineWidth, 20);
            String indirimOraniStr = String.format("%.0f", labelDto.getIndirimOrani());
            printer.printAndroidFont(180, 280, Typeface.DEFAULT, true, false, "%" + indirimOraniStr, nLineWidth, 38);

            if (labelDto.getYerliUretim() == 0) {
                final String yerliUretimLogo = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/MobiltegDB/yerliUretim100.png";
                try {
                    printer.printBitmap(yerliUretimLogo, 5, 330);
                } catch (Exception e) {

                }
            }

            printer.printAndroidFont(5, 375, Typeface.DEFAULT, true, false, "Fiyat Değ.Tarihi:", nLineWidth, 18);
            String fiyatDegisiklikTarihi = getFormattedDate(labelDto.getFiyatDegTarihi().substring(0, 11));
            printer.printAndroidFont(140, 375, Typeface.DEFAULT, true, false, fiyatDegisiklikTarihi, nLineWidth, 18);
            printer.printAndroidFont(115, 330, Typeface.DEFAULT, true, false, "Menşei: ", nLineWidth, 18);
            printer.printAndroidFont(115, 350, Typeface.DEFAULT, true, false, labelDto.getMensei(), nLineWidth, 18);

            printer.printCPCLText(CPCLConst.LK_CPCL_0_ROTATION, CPCLConst.LK_CPCL_FONT_7, 0, 80,230, "KDV DAHILDIR", 1);


            printer.printForm();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException exception) {
            exception.printStackTrace();
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
            printer.printAndroidFont(0, 270, Typeface.DEFAULT, true, false, "KDV DAHİLDİR", nLineWidth, 20);
            printer.printAndroidFont(0, 295, Typeface.DEFAULT, true, false, "Menşei: " + labelDto.getMensei(), nLineWidth, 20);

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

                printer.printAndroidFont(175, 195, Typeface.DEFAULT, false, false, beden, nLineWidth, 20);
            }

            String satiFiyati = String.format("%.2f", labelDto.getSatisFiyati());
            printer.printCPCLText(CPCLConst.LK_CPCL_0_ROTATION, 5, 3, 15, 160, satiFiyati, 0);
            int tlOteleme = (int) ((satiFiyati.length() + 0.5) * 2.2 * 8);
            printer.printAndroidFont(15 + tlOteleme, 180, Typeface.DEFAULT, false, false, "₺", nLineWidth, 50);
            printer.printAndroidFont(32 + tlOteleme, 220, Typeface.DEFAULT, true, false, "KDV DAHİLDİR", nLineWidth, 18);

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

    public void printDikeyBarkodluBeyazEtiket(LabelDto labelDto, int count) {
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
                printer.printCPCLBarcode(CPCLConst.LK_CPCL_90_ROTATION, CPCLConst.LK_CPCL_BCS_EAN13, 1, CPCLConst.LK_CPCL_BCS_1RATIO, 50, 0, 300, labelDto.getBarkod(), 0);
            } else {
                printer.printCPCLBarcode(CPCLConst.LK_CPCL_90_ROTATION, CPCLConst.LK_CPCL_BCS_128, 1, CPCLConst.LK_CPCL_BCS_1RATIO, 50, 0, 300, labelDto.getBarkod(), 0);
            }
            printer.printCPCLText(CPCLConst.LK_CPCL_90_ROTATION, 7, 0, 55, 275, labelDto.getBarkod(),0);



            String beden = labelDto.getBeden();
            if (beden != null && !beden.isEmpty()) {
                printer.printCPCLText(CPCLConst.LK_CPCL_0_ROTATION, 7, 0, 85, 190, "BEDEN", 0);

                printer.printAndroidFont(85, 210, Typeface.DEFAULT, false, false, beden, nLineWidth, 20);
            }

            String satiFiyati = String.format("%.2f", labelDto.getSatisFiyati());
            printer.printCPCLText(CPCLConst.LK_CPCL_0_ROTATION, 5, 3, 85, 90, satiFiyati, 0);
            int tlOteleme = (int) ((satiFiyati.length() + 0.5) * 2.2 * 8);
            printer.printAndroidFont(85 + tlOteleme, 110, Typeface.DEFAULT, false, false, "₺", nLineWidth, 50);
            printer.printAndroidFont(85, 170, Typeface.DEFAULT, true, false, "KDV DAHİLDİR", nLineWidth, 18);

            if (labelDto.getYerliUretim() == 0) {
                final String yerliUretimLogo = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/MobiltegDB/yerliUretim100.png";
                try {
                    printer.printBitmap(yerliUretimLogo,155 , 190);
                    printer.setMedia(paperType);
                } catch (Exception e) {

                }

            }
            printer.printAndroidFont(103, 265, Typeface.DEFAULT, true, false, "Fiyat Değ.Tarihi", nLineWidth, 20);
            String fiyatDegisiklikTarihi = getFormattedDate(labelDto.getFiyatDegTarihi().substring(0, 11));
            printer.printAndroidFont(103, 285, Typeface.DEFAULT, true, false, fiyatDegisiklikTarihi, nLineWidth, 20);
            printer.printAndroidFont(100, 230, Typeface.DEFAULT, true, false, "Menşei: " + labelDto.getMensei(), nLineWidth, 20);

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


            printer.printAndroidFont(55, 130, Typeface.SANS_SERIF, true, false, "Peşin Fiyat", nLineWidth, 38);
            printer.printAndroidFont(55, 175, Typeface.SANS_SERIF, true, false, satisFiyatiStr, nLineWidth, 38);
            tlOteleme = (int) ((satisFiyatiStr.length() + 0.5) * 2.2 * 8);
            printer.printAndroidFont(55 + tlOteleme, 175, Typeface.DEFAULT, true, false, "₺", nLineWidth, 38);
            printer.printAndroidFont(55, 215, Typeface.SANS_SERIF, true, false, "KDV DAHİLDİR", nLineWidth, 18);

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

            printer.printCPCLText(CPCLConst.LK_CPCL_90_ROTATION, CPCLConst.LK_CPCL_FONT_7, 1, 5,240, "KDV DAHILDIR", 1);
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
            printer.setMedia(CPCLConst.LK_CPCL_BLACKMARK);

            // Stok Adı
            printer.printAndroidFont(Typeface.SANS_SERIF, true, labelDto.getStokAdi(), nLineWidth, 26, 40, CPCLConst.LK_CPCL_LEFT);


            printer.printAndroidFont(Typeface.SANS_SERIF, true, "(Fiyatlara KDV Dahildir)", nLineWidth, 26, 65, CPCLConst.LK_CPCL_LEFT);

            // Yerli üretim logosu
            if (labelDto.getYerliUretim() == 0) {
                final String yerliUretimLogo = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/MobilEtiket/yerliUretim120.png";
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


    public void printZuccaciyeRafEtiket(LabelDto labelDto, int count) {

        try {
            int nLineWidth = 570;
            printer.setForm(0, 200, 200, 304, count);
            printer.setPageWidth(640);
            printer.setMedia(CPCLConst.LK_CPCL_LABEL);

            // LK_CPCL_BLACKMARK

            // Stok Adı
            printer.printAndroidFont(Typeface.SANS_SERIF, true, labelDto.getStokKisaAdi(), nLineWidth, 35, 20, CPCLConst.LK_CPCL_CENTER);


            // Yerli üretim logosu
            if (labelDto.getYerliUretim() == 0) {
                final String yerliUretimLogo = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/MobilEtiket/yerliUretim120.png";
                try {
                    printer.printBitmap(yerliUretimLogo, 445, 150);
                } catch (Exception e) {
                }
            }

            // Fiyat Değişim Tarihi
            printer.printAndroidFont(Typeface.SANS_SERIF, true, "Üretim Yeri: " + labelDto.getMensei(), nLineWidth, 16, 200, CPCLConst.LK_CPCL_RIGHT);
            printer.printAndroidFont(Typeface.SANS_SERIF, true, "Fiyat Değişim Tarihi", nLineWidth, 16, 220, CPCLConst.LK_CPCL_RIGHT);
            String fiyatDegisiklikTarihi = getFormattedDate(labelDto.getFiyatDegTarihi().substring(0, 11));
            printer.printAndroidFont(Typeface.SANS_SERIF, true, fiyatDegisiklikTarihi, nLineWidth, 18, 240, CPCLConst.LK_CPCL_RIGHT);
            // Satış fiyatı tutar
            String satisFiyatiStr = String.format("%.2f₺", labelDto.getSatisFiyati());

            printer.printAndroidFont(Typeface.SANS_SERIF, true, satisFiyatiStr, nLineWidth, 65, 175, CPCLConst.LK_CPCL_CENTER);
            printer.printAndroidFont( Typeface.SANS_SERIF,  false, "FİYATLARA KDV DAHİLDİR", nLineWidth, 18,250, CPCLConst.LK_CPCL_CENTER);

            StringBuffer buffer = printer.getBuffer();


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
            printer.setMedia(CPCLConst.LK_CPCL_BLACKMARK);

            // Stok Adı
            printer.printAndroidFont(Typeface.SANS_SERIF, true, labelDto.getStokAdi(), nLineWidth, 26, 40, CPCLConst.LK_CPCL_LEFT);

            printer.printAndroidFont(Typeface.SANS_SERIF, true, "(Fiyatlara KDV Dahildir)", nLineWidth, 26, 65, CPCLConst.LK_CPCL_LEFT);

            // Yerli üretim logosu
            if (labelDto.getEtiketFiyati() == 0) {
                final String yerliUretimLogo = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/MobilEtiket/yerliUretim120.jpg";
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

    public void printKirmiziBarkodluTaksitliEtiket(LabelDto labelDto, int count) {
        try {
            int nLineWidth = 400;
            printer.setForm(0, 400, 512, 512, count);
            printer.setMedia(paperType);

           // printer.printAndroidFont(55, 45, Typeface.SANS_SERIF, false, false, labelDto.getBarkod(), nLineWidth, 20);
            printer.printCPCLText(CPCLConst.LK_CPCL_270_ROTATION, CPCLConst.LK_CPCL_FONT_7, 0, 25,70, labelDto.getBarkod(), 1);
            if(labelDto.getBarkod().length() > 11){
                printer.printCPCLBarcode(CPCLConst.LK_CPCL_90_ROTATION,
                        CPCLConst.LK_CPCL_BCS_EAN13,
                        1, CPCLConst.LK_CPCL_BCS_0RATIO,
                        50, 25, 260, labelDto.getBarkod(), 0);
            } else {
                printer.printCPCLBarcode(CPCLConst.LK_CPCL_90_ROTATION,
                        CPCLConst.LK_CPCL_BCS_128,
                        1, CPCLConst.LK_CPCL_BCS_0RATIO,
                        50, 25, 260, labelDto.getBarkod(), 0);
            }
            int stokAdiLength = labelDto.getStokAdi().length();

            String stokAdiSatir1 = " ";
            String stokAdiSatir2 = " ";
            String stokAdiSatir3 = " ";
            if (stokAdiLength <= 25) {
                stokAdiSatir1 = labelDto.getStokAdi().substring(0, stokAdiLength - 1).trim();
            } else if (stokAdiLength <= 50) {
                stokAdiSatir1 = labelDto.getStokAdi().substring(0, 25).trim();
                stokAdiSatir2 = labelDto.getStokAdi().substring(25, stokAdiLength - 1).trim();
            } else if (stokAdiLength < 75) {
                stokAdiSatir1 = labelDto.getStokAdi().substring(0, 25).trim();
                stokAdiSatir2 = labelDto.getStokAdi().substring(25, 50).trim();
                stokAdiSatir3 = labelDto.getStokAdi().substring(50, stokAdiLength - 1).trim();
            } else {
                stokAdiSatir1 = labelDto.getStokAdi().substring(0, 25).trim();
                stokAdiSatir2 = labelDto.getStokAdi().substring(25, 50).trim();
                stokAdiSatir3 = labelDto.getStokAdi().substring(50, 75).trim();
            }

            String satisFiyatiStr = String.format("%.2f", labelDto.getSatisFiyati());
            String taksitliFiyatStr = String.format("%.2f", labelDto.getTaksitFiyati());
            String etiketFiyatStr = String.format("%.2f", labelDto.getEtiketFiyati());

            if(!stokAdiSatir1.isEmpty())
                printer.printAndroidFont(5, 0, Typeface.SANS_SERIF, false, false, stokAdiSatir1, nLineWidth, 24);

            if(!stokAdiSatir2.isEmpty())
                printer.printAndroidFont(5, 20, Typeface.SANS_SERIF, false, false, stokAdiSatir2, nLineWidth, 24);

            if(!stokAdiSatir3.isEmpty())
                printer.printAndroidFont(5, 40, Typeface.SANS_SERIF, false, false, stokAdiSatir3, nLineWidth, 24);

            printer.printAndroidFont(80, 65, Typeface.SANS_SERIF, true, false, "Satış Fiyatı", nLineWidth, 28);
            printer.printAndroidFont(80, 92, Typeface.SANS_SERIF, true, false, etiketFiyatStr, nLineWidth, 28);
            int tlOteleme = (int) ((etiketFiyatStr.length() + 0.5) * 1.6 * 8);
            printer.printAndroidFont(80 + tlOteleme, 92, Typeface.DEFAULT, true, false, "₺", nLineWidth, 28);

            if (etiketFiyatStr.length() <= 5) {
                printer.printLine(80, 95, 150, 120, 5);
                printer.printLine(80, 120, 150, 95, 5);
            } else {
                printer.printLine(80, 95, 170, 120, 5);
                printer.printLine(80, 120, 170, 95, 5);
            }


            printer.printAndroidFont(80, 127, Typeface.SANS_SERIF, true, false, "Taksitli Fiyat", nLineWidth, 28);
            printer.printAndroidFont(80, 155, Typeface.SANS_SERIF, true, false, taksitliFiyatStr, nLineWidth, 28);
            tlOteleme = (int) ((taksitliFiyatStr.length() + 0.5) * 1.6 * 8);
            printer.printAndroidFont(80 + tlOteleme, 155, Typeface.DEFAULT, true, false, "₺", nLineWidth, 28);

            printer.printAndroidFont(80, 190, Typeface.SANS_SERIF, true, false, "İnd. Satış Fiyat", nLineWidth, 28);
            printer.printAndroidFont(80, 225, Typeface.SANS_SERIF, true, false, satisFiyatiStr, nLineWidth, 38);
            tlOteleme = (int) ((satisFiyatiStr.length() + 0.5) * 2.2 * 8);
            printer.printAndroidFont(80 + tlOteleme, 225, Typeface.DEFAULT, true, false, "₺", nLineWidth, 38);

            String beden = labelDto.getBeden();
            if (beden != null && !beden.isEmpty()) {
                printer.printAndroidFont(5, 275, Typeface.DEFAULT, false, false, "BEDEN", nLineWidth, 20);
                printer.printAndroidFont(5, 295, Typeface.DEFAULT, true, false, beden, nLineWidth, 24);
            }

            printer.printAndroidFont(90, 275, Typeface.DEFAULT, false, false, "İNDİRİM", nLineWidth, 20);
            printer.printAndroidFont(90, 295, Typeface.DEFAULT, false, false, "ORANI", nLineWidth, 20);
            String indirimOraniStr = String.format("%.0f", labelDto.getIndirimOrani());
            printer.printAndroidFont(180, 280, Typeface.DEFAULT, true, false, "%" + indirimOraniStr, nLineWidth, 38);

            if (labelDto.getYerliUretim() == 0) {
                final String yerliUretimLogo = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/MobiltegDB/yerliUretim100.png";
                try {
                    printer.printBitmap(yerliUretimLogo, 5, 330);
                } catch (Exception e) {

                }
            }

            printer.printAndroidFont(5, 375, Typeface.DEFAULT, true, false, "Fiyat Değ.Tarihi:", nLineWidth, 18);
            String fiyatDegisiklikTarihi = getFormattedDate(labelDto.getFiyatDegTarihi().substring(0, 11));
            printer.printAndroidFont(140, 375, Typeface.DEFAULT, true, false, fiyatDegisiklikTarihi, nLineWidth, 18);
            printer.printAndroidFont(115, 330, Typeface.DEFAULT, true, false, "Menşei: ", nLineWidth, 18);
            printer.printAndroidFont(115, 350, Typeface.DEFAULT, true, false, labelDto.getMensei(), nLineWidth, 18);

            printer.printCPCLText(CPCLConst.LK_CPCL_90_ROTATION, CPCLConst.LK_CPCL_FONT_7, 0, 265,305, "KDV DAHILDIR", 1);


            printer.printForm();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void printKirimiziBarkodsuzTaksitliEtiket(LabelDto labelDto, int count) {
        try {
            int nLineWidth = 400;
            printer.setForm(0, 400, 512, 512, count);
            printer.setMedia(paperType);

            int stokAdiLength = labelDto.getStokAdi().length();

            String stokAdiSatir1 = " ";
            String stokAdiSatir2 = " ";
            String stokAdiSatir3 = " ";
            if (stokAdiLength <= 25) {
                stokAdiSatir1 = labelDto.getStokAdi().substring(0, stokAdiLength - 1).trim();
            } else if (stokAdiLength <= 50) {
                stokAdiSatir1 = labelDto.getStokAdi().substring(0, 25).trim();
                stokAdiSatir2 = labelDto.getStokAdi().substring(25, stokAdiLength - 1).trim();
            } else if (stokAdiLength < 75) {
                stokAdiSatir1 = labelDto.getStokAdi().substring(0, 25).trim();
                stokAdiSatir2 = labelDto.getStokAdi().substring(25, 50).trim();
                stokAdiSatir3 = labelDto.getStokAdi().substring(50, stokAdiLength - 1).trim();
            } else {
                stokAdiSatir1 = labelDto.getStokAdi().substring(0, 25).trim();
                stokAdiSatir2 = labelDto.getStokAdi().substring(25, 50).trim();
                stokAdiSatir3 = labelDto.getStokAdi().substring(50, 75).trim();
            }

            String satisFiyatiStr = String.format("%.2f", labelDto.getSatisFiyati());
            String taksitliFiyatStr = String.format("%.2f", labelDto.getTaksitFiyati());
            String etiketFiyatStr = String.format("%.2f", labelDto.getEtiketFiyati());

            printer.printCPCLText(CPCLConst.LK_CPCL_90_ROTATION, CPCLConst.LK_CPCL_FONT_7, 1, 15,250, "KDV DAHILDIR", 1);

            if(!stokAdiSatir1.isEmpty())
                printer.printAndroidFont(5, 0, Typeface.SANS_SERIF, false, false, stokAdiSatir1, nLineWidth, 24);

            if(!stokAdiSatir2.isEmpty())
                printer.printAndroidFont(5, 20, Typeface.SANS_SERIF, false, false, stokAdiSatir2, nLineWidth, 24);

            if(!stokAdiSatir3.isEmpty())
                printer.printAndroidFont(5, 40, Typeface.SANS_SERIF, false, false, stokAdiSatir3, nLineWidth, 24);

            printer.printAndroidFont(80, 65, Typeface.SANS_SERIF, true, false, "Satış Fiyatı", nLineWidth, 28);
            printer.printAndroidFont(80, 92, Typeface.SANS_SERIF, true, false, etiketFiyatStr, nLineWidth, 28);
            int tlOteleme = (int) ((etiketFiyatStr.length() + 0.5) * 1.6 * 8);
            printer.printAndroidFont(80 + tlOteleme, 92, Typeface.DEFAULT, true, false, "₺", nLineWidth, 28);

            if (etiketFiyatStr.length() <= 5) {
                printer.printLine(80, 95, 150, 120, 5);
                printer.printLine(80, 120, 150, 95, 5);
            } else {
                printer.printLine(80, 95, 170, 120, 5);
                printer.printLine(80, 120, 170, 95, 5);
            }


            printer.printAndroidFont(80, 127, Typeface.SANS_SERIF, true, false, "Taksitli Fiyat", nLineWidth, 28);
            printer.printAndroidFont(80, 155, Typeface.SANS_SERIF, true, false, taksitliFiyatStr, nLineWidth, 28);
            tlOteleme = (int) ((taksitliFiyatStr.length() + 0.5) * 1.6 * 8);
            printer.printAndroidFont(80 + tlOteleme, 155, Typeface.DEFAULT, true, false, "₺", nLineWidth, 28);

            printer.printAndroidFont(80, 190, Typeface.SANS_SERIF, true, false, "İnd. Satış Fiyat", nLineWidth, 28);
            printer.printAndroidFont(80, 225, Typeface.SANS_SERIF, true, false, satisFiyatiStr, nLineWidth, 38);
            tlOteleme = (int) ((satisFiyatiStr.length() + 0.5) * 2.2 * 8);
            printer.printAndroidFont(80 + tlOteleme, 225, Typeface.DEFAULT, true, false, "₺", nLineWidth, 38);

            printer.printAndroidFont(90, 275, Typeface.DEFAULT, false, false, "İNDİRİM", nLineWidth, 20);
            printer.printAndroidFont(90, 295, Typeface.DEFAULT, false, false, "ORANI", nLineWidth, 20);
            String indirimOraniStr = String.format("%.0f", labelDto.getIndirimOrani());
            printer.printAndroidFont(180, 280, Typeface.DEFAULT, true, false, "%" + indirimOraniStr, nLineWidth, 38);

            if (labelDto.getYerliUretim() == 0) {
                final String yerliUretimLogo = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/MobiltegDB/yerliUretim100.png";
                try {
                    printer.printBitmap(yerliUretimLogo, 5, 330);
                } catch (Exception e) {

                }
            }

            printer.printAndroidFont(5, 375, Typeface.DEFAULT, true, false, "Fiyat Değ.Tarihi:", nLineWidth, 18);
            String fiyatDegisiklikTarihi = getFormattedDate(labelDto.getFiyatDegTarihi().substring(0, 11));
            printer.printAndroidFont(140, 375, Typeface.DEFAULT, true, false, fiyatDegisiklikTarihi, nLineWidth, 18);
            printer.printAndroidFont(115, 330, Typeface.DEFAULT, true, false, "Menşei: ", nLineWidth, 18);
            printer.printAndroidFont(115, 350, Typeface.DEFAULT, true, false, labelDto.getMensei(), nLineWidth, 18);


            printer.printForm();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void printBarkodluKirmiziEtiket_50(LabelDto labelDto, int count) {
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
            String etiketFiyati = String.format("%.2f", labelDto.getSatisFiyati());
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


            String satisFiyati = String.format("%.5f", labelDto.getSatisFiyati()/2);
            satisFiyati = satisFiyati.substring(0,satisFiyati.length()-3);

            printer.printAndroidFont(0, 220, Typeface.DEFAULT, true, false, "KASA FIYATI", nLineWidth, 24);
            printer.printCPCLText(CPCLConst.LK_CPCL_0_ROTATION, 5, 3, 0, 240, satisFiyati, 0);
            tlOteleme = (int) ((satisFiyati.length() + 0.5) * 2.2 * 8);
            printer.printAndroidFont(0 + tlOteleme, 265, Typeface.DEFAULT, false, false, "₺", nLineWidth, 50);

            printer.printAndroidFont(200, 195, Typeface.DEFAULT, false, false, "İNDİRİM", nLineWidth, 24);
            printer.printAndroidFont(200, 215, Typeface.DEFAULT, false, false, "ORANI", nLineWidth, 24);
            String indirimOraniStr = String.format("%.0f",50.0);
            printer.printAndroidFont(180, 230, Typeface.DEFAULT, true, false, "%" + indirimOraniStr, nLineWidth, 50);
            printer.printAndroidFont(165, 280, Typeface.DEFAULT, true, false, "KDV DAHİLDİR", nLineWidth, 18);

            if (labelDto.getYerliUretim() == 0) {
                final String yerliUretimLogo = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/MobilEtiket/yerliUretim100.png";
                printer.printBitmap(yerliUretimLogo, 0, 320);
            }

            printer.printAndroidFont(120, 315, Typeface.DEFAULT, true, false, "Fiyat Değ.Tarihi", nLineWidth, 20);
            String fiyatDegisiklikTarihi = getFormattedDate(labelDto.getFiyatDegTarihi().substring(0, 11));
            printer.printAndroidFont(120, 340, Typeface.DEFAULT, false, false, fiyatDegisiklikTarihi, nLineWidth, 20);

            printer.printAndroidFont(0, 365, Typeface.DEFAULT, true, false, "Menşei: " + labelDto.getMensei(), nLineWidth, 24);
            printer.printForm();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printIndirimliRafEtiket_50(LabelDto labelDto, int count) {

        try {
            int nLineWidth = 384;
            printer.setForm(0, 384, 416, 416, count);
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
            //String etiketFiyati = String.format("%.2f", labelDto.getEtiketFiyati());
            String satisFiyati = String.format("%.2f", labelDto.getSatisFiyati());
//            printer.printCPCLText(CPCLConst.LK_CPCL_0_ROTATION, 5, 3, 0, 135, etiketFiyati, 0);
            printer.printCPCLText(CPCLConst.LK_CPCL_0_ROTATION, 5, 3, 0, 135, satisFiyati, 0);
            int tlOteleme = (int) ((satisFiyati.length() + 0.5) * 2.2 * 8);
            printer.printAndroidFont(0 + tlOteleme, 155, Typeface.DEFAULT, false, false, "₺", nLineWidth, 50);
            if (satisFiyati.length() <= 5) {
                printer.printLine(0, 145, 134, 210, 5);
                printer.printLine(134, 145, 0, 210, 5);
            } else {
                printer.printLine(0, 145, 164, 210, 5);
                printer.printLine(164, 145, 0, 210, 5);
            }

            String satisFiyati2 = String.format("%.5f", labelDto.getSatisFiyati()/2);
            satisFiyati2 = satisFiyati2.substring(0,satisFiyati2.length()-3);

            printer.printAndroidFont(0, 220, Typeface.DEFAULT, true, false, "KASA FIYATI", nLineWidth, 24);
            printer.printCPCLText(CPCLConst.LK_CPCL_0_ROTATION, 5, 3, 0, 240, satisFiyati2, 0);
            tlOteleme = (int) ((satisFiyati2.length() + 0.5) * 2.2 * 8);
            printer.printAndroidFont(0 + tlOteleme, 265, Typeface.DEFAULT, false, false, "₺", nLineWidth, 50);


            printer.printAndroidFont(200, 155, Typeface.DEFAULT, false, false, "İNDİRİM", nLineWidth, 24);
            printer.printAndroidFont(200, 180, Typeface.DEFAULT, false, false, "ORANI", nLineWidth, 24);
            String indirimOraniStr = "50";    //String.format("%.0f",50);
//            String indirimOraniStr = String.format("%.0f", labelDto.getIndirimOrani());
            printer.printAndroidFont(180, 205, Typeface.DEFAULT, true, false, "%" + indirimOraniStr, nLineWidth, 50);
            printer.printAndroidFont(165, 280, Typeface.DEFAULT, true, false, "KDV DAHİLDİR", nLineWidth, 18);

            if (labelDto.getYerliUretim() == 0) {
                final String yerliUretimLogo = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/MobilEtiket/yerliUretim100.png";
                printer.printBitmap(yerliUretimLogo, 0, 320);
            }

            printer.printAndroidFont(120, 315, Typeface.DEFAULT, true, false, "Fiyat Değ.Tarihi", nLineWidth, 20);
            String fiyatDegisiklikTarihi = getFormattedDate(labelDto.getFiyatDegTarihi().substring(0, 11));
            printer.printAndroidFont(120, 340, Typeface.DEFAULT, true, false, fiyatDegisiklikTarihi, nLineWidth, 20);

            printer.printAndroidFont(0, 365, Typeface.DEFAULT, true, false, "Menşei: " + labelDto.getMensei(), nLineWidth, 24);
            printer.printForm();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void printKirimiziBarkodsuzTaksitliEtiket_50(LabelDto labelDto, int count) {
        try {
            int nLineWidth = 400;
            printer.setForm(0, 400, 512, 512, count);
            printer.setMedia(paperType);

            int stokAdiLength = labelDto.getStokAdi().length();

            String stokAdiSatir1 = " ";
            String stokAdiSatir2 = " ";
            String stokAdiSatir3 = " ";
            if (stokAdiLength <= 25) {
                stokAdiSatir1 = labelDto.getStokAdi().substring(0, stokAdiLength - 1).trim();
            } else if (stokAdiLength <= 50) {
                stokAdiSatir1 = labelDto.getStokAdi().substring(0, 25).trim();
                stokAdiSatir2 = labelDto.getStokAdi().substring(25, stokAdiLength - 1).trim();
            } else if (stokAdiLength < 75) {
                stokAdiSatir1 = labelDto.getStokAdi().substring(0, 25).trim();
                stokAdiSatir2 = labelDto.getStokAdi().substring(25, 50).trim();
                stokAdiSatir3 = labelDto.getStokAdi().substring(50, stokAdiLength - 1).trim();
            } else {
                stokAdiSatir1 = labelDto.getStokAdi().substring(0, 25).trim();
                stokAdiSatir2 = labelDto.getStokAdi().substring(25, 50).trim();
                stokAdiSatir3 = labelDto.getStokAdi().substring(50, 75).trim();
            }
//            double satis_fiyati = ((labelDto.getSatisFiyati()/2)*100)/100;


            String satisFiyatiStr = String.format("%.5f", labelDto.getSatisFiyati()/2);
            satisFiyatiStr = satisFiyatiStr.substring(0,satisFiyatiStr.length()-3);

//            String satisFiyatiStr = String.format("%.2f", labelDto.getSatisFiyati()/2.0);
            String taksitliFiyatStr = String.format("%.5f", labelDto.getTaksitFiyati()/2.0);
            taksitliFiyatStr = taksitliFiyatStr.substring(0,taksitliFiyatStr.length()-3);

            String etiketFiyatStr = String.format("%.2f", labelDto.getSatisFiyati());

            if(!stokAdiSatir1.isEmpty())
                printer.printAndroidFont(5, 0, Typeface.SANS_SERIF, false, false, stokAdiSatir1, nLineWidth, 24);

            if(!stokAdiSatir2.isEmpty())
                printer.printAndroidFont(5, 20, Typeface.SANS_SERIF, false, false, stokAdiSatir2, nLineWidth, 24);

            if(!stokAdiSatir3.isEmpty())
                printer.printAndroidFont(5, 40, Typeface.SANS_SERIF, false, false, stokAdiSatir3, nLineWidth, 24);

            printer.printAndroidFont(80, 65, Typeface.SANS_SERIF, true, false, "Satış Fiyatı", nLineWidth, 28);
            printer.printAndroidFont(80, 92, Typeface.SANS_SERIF, true, false, etiketFiyatStr, nLineWidth, 28);
            int tlOteleme = (int) ((etiketFiyatStr.length() + 0.5) * 1.6 * 8);
            printer.printAndroidFont(80 + tlOteleme, 92, Typeface.DEFAULT, true, false, "₺", nLineWidth, 28);

            if (etiketFiyatStr.length() <= 5) {
                printer.printLine(80, 95, 150, 120, 5);
                printer.printLine(80, 120, 150, 95, 5);
            } else {
                printer.printLine(80, 95, 170, 120, 5);
                printer.printLine(80, 120, 170, 95, 5);
            }


            printer.printAndroidFont(80, 127, Typeface.SANS_SERIF, true, false, "Taksitli Fiyat", nLineWidth, 28);
            printer.printAndroidFont(80, 155, Typeface.SANS_SERIF, true, false, taksitliFiyatStr, nLineWidth, 28);
            tlOteleme = (int) ((taksitliFiyatStr.length() + 0.5) * 1.6 * 8);
            printer.printAndroidFont(80 + tlOteleme, 155, Typeface.DEFAULT, true, false, "₺", nLineWidth, 28);

            printer.printAndroidFont(80, 190, Typeface.SANS_SERIF, true, false, "İnd. Satış Fiyat", nLineWidth, 28);
            printer.printAndroidFont(80, 225, Typeface.SANS_SERIF, true, false, satisFiyatiStr, nLineWidth, 38);
            tlOteleme = (int) ((satisFiyatiStr.length() + 0.5) * 2.2 * 8);
            printer.printAndroidFont(80 + tlOteleme, 225, Typeface.DEFAULT, true, false, "₺", nLineWidth, 38);

            printer.printAndroidFont(90, 275, Typeface.DEFAULT, false, false, "İNDİRİM", nLineWidth, 20);
            printer.printAndroidFont(90, 295, Typeface.DEFAULT, false, false, "ORANI", nLineWidth, 20);
            String indirimOraniStr = String.format("%.0f", 50.0);
            printer.printAndroidFont(180, 280, Typeface.DEFAULT, true, false, "%" + indirimOraniStr, nLineWidth, 38);


            if (labelDto.getYerliUretim() == 0) {
                final String yerliUretimLogo = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/MobiltegDB/yerliUretim100.png";
                try {
                    printer.printBitmap(yerliUretimLogo, 5, 330);
                } catch (Exception e) {

                }
            }

            printer.printAndroidFont(5, 375, Typeface.DEFAULT, true, false, "Fiyat Değ.Tarihi:", nLineWidth, 18);
            String fiyatDegisiklikTarihi = getFormattedDate(labelDto.getFiyatDegTarihi().substring(0, 11));
            printer.printAndroidFont(140, 375, Typeface.DEFAULT, true, false, fiyatDegisiklikTarihi, nLineWidth, 18);
            printer.printAndroidFont(115, 330, Typeface.DEFAULT, true, false, "Menşei: ", nLineWidth, 18);
            printer.printAndroidFont(115, 350, Typeface.DEFAULT, true, false, labelDto.getMensei(), nLineWidth, 18);


            printer.printForm();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void printKirmiziBarkodluTaksitliEtiket_50(LabelDto labelDto, int count) {
        try {
            int nLineWidth = 400;
            printer.setForm(0, 400, 512, 512, count);
            printer.setMedia(paperType);

            // printer.printAndroidFont(55, 45, Typeface.SANS_SERIF, false, false, labelDto.getBarkod(), nLineWidth, 20);
            printer.printCPCLText(CPCLConst.LK_CPCL_270_ROTATION, CPCLConst.LK_CPCL_FONT_7, 0, 25,70, labelDto.getBarkod(), 1);
            if(labelDto.getBarkod().length() > 11){
                printer.printCPCLBarcode(CPCLConst.LK_CPCL_90_ROTATION,
                        CPCLConst.LK_CPCL_BCS_EAN13,
                        1, CPCLConst.LK_CPCL_BCS_0RATIO,
                        50, 25, 260, labelDto.getBarkod(), 0);
            } else {
                printer.printCPCLBarcode(CPCLConst.LK_CPCL_90_ROTATION,
                        CPCLConst.LK_CPCL_BCS_128,
                        1, CPCLConst.LK_CPCL_BCS_0RATIO,
                        50, 25, 260, labelDto.getBarkod(), 0);
            }
            int stokAdiLength = labelDto.getStokAdi().length();

            String stokAdiSatir1 = " ";
            String stokAdiSatir2 = " ";
            String stokAdiSatir3 = " ";
            if (stokAdiLength <= 25) {
                stokAdiSatir1 = labelDto.getStokAdi().substring(0, stokAdiLength - 1).trim();
            } else if (stokAdiLength <= 50) {
                stokAdiSatir1 = labelDto.getStokAdi().substring(0, 25).trim();
                stokAdiSatir2 = labelDto.getStokAdi().substring(25, stokAdiLength - 1).trim();
            } else if (stokAdiLength < 75) {
                stokAdiSatir1 = labelDto.getStokAdi().substring(0, 25).trim();
                stokAdiSatir2 = labelDto.getStokAdi().substring(25, 50).trim();
                stokAdiSatir3 = labelDto.getStokAdi().substring(50, stokAdiLength - 1).trim();
            } else {
                stokAdiSatir1 = labelDto.getStokAdi().substring(0, 25).trim();
                stokAdiSatir2 = labelDto.getStokAdi().substring(25, 50).trim();
                stokAdiSatir3 = labelDto.getStokAdi().substring(50, 75).trim();
            }

            String satisFiyatiStr = String.format("%.2f", labelDto.getSatisFiyati());
            String taksitliFiyatStr = String.format("%.2f", labelDto.getTaksitFiyati());
            String etiketFiyatStr = String.format("%.2f", labelDto.getEtiketFiyati());

            if(!stokAdiSatir1.isEmpty())
                printer.printAndroidFont(5, 0, Typeface.SANS_SERIF, false, false, stokAdiSatir1, nLineWidth, 24);

            if(!stokAdiSatir2.isEmpty())
                printer.printAndroidFont(5, 20, Typeface.SANS_SERIF, false, false, stokAdiSatir2, nLineWidth, 24);

            if(!stokAdiSatir3.isEmpty())
                printer.printAndroidFont(5, 40, Typeface.SANS_SERIF, false, false, stokAdiSatir3, nLineWidth, 24);

            printer.printAndroidFont(80, 65, Typeface.SANS_SERIF, true, false, "Satış Fiyatı", nLineWidth, 28);
            printer.printAndroidFont(80, 92, Typeface.SANS_SERIF, true, false, etiketFiyatStr, nLineWidth, 28);
            int tlOteleme = (int) ((etiketFiyatStr.length() + 0.5) * 1.6 * 8);
            printer.printAndroidFont(80 + tlOteleme, 92, Typeface.DEFAULT, true, false, "₺", nLineWidth, 28);

            if (etiketFiyatStr.length() <= 5) {
                printer.printLine(80, 95, 150, 120, 5);
                printer.printLine(80, 120, 150, 95, 5);
            } else {
                printer.printLine(80, 95, 170, 120, 5);
                printer.printLine(80, 120, 170, 95, 5);
            }


            printer.printAndroidFont(80, 127, Typeface.SANS_SERIF, true, false, "Taksitli Fiyat", nLineWidth, 28);
            printer.printAndroidFont(80, 155, Typeface.SANS_SERIF, true, false, taksitliFiyatStr, nLineWidth, 28);
            tlOteleme = (int) ((taksitliFiyatStr.length() + 0.5) * 1.6 * 8);
            printer.printAndroidFont(80 + tlOteleme, 155, Typeface.DEFAULT, true, false, "₺", nLineWidth, 28);

            printer.printAndroidFont(80, 190, Typeface.SANS_SERIF, true, false, "İnd. Satış Fiyat", nLineWidth, 28);
            printer.printAndroidFont(80, 225, Typeface.SANS_SERIF, true, false, satisFiyatiStr, nLineWidth, 38);
            tlOteleme = (int) ((satisFiyatiStr.length() + 0.5) * 2.2 * 8);
            printer.printAndroidFont(80 + tlOteleme, 225, Typeface.DEFAULT, true, false, "₺", nLineWidth, 38);

            String beden = labelDto.getBeden();
            if (beden != null && !beden.isEmpty()) {
                printer.printAndroidFont(5, 275, Typeface.DEFAULT, false, false, "BEDEN", nLineWidth, 20);
                printer.printAndroidFont(5, 295, Typeface.DEFAULT, true, false, beden, nLineWidth, 24);
            }

            printer.printAndroidFont(90, 275, Typeface.DEFAULT, false, false, "İNDİRİM", nLineWidth, 20);
            printer.printAndroidFont(90, 295, Typeface.DEFAULT, false, false, "ORANI", nLineWidth, 20);
            String indirimOraniStr = String.format("%.0f", labelDto.getIndirimOrani());
            printer.printAndroidFont(180, 280, Typeface.DEFAULT, true, false, "%" + indirimOraniStr, nLineWidth, 38);

            if (labelDto.getYerliUretim() == 0) {
                final String yerliUretimLogo = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/MobiltegDB/yerliUretim100.png";
                try {
                    printer.printBitmap(yerliUretimLogo, 5, 330);
                } catch (Exception e) {

                }
            }

            printer.printAndroidFont(5, 375, Typeface.DEFAULT, true, false, "Fiyat Değ.Tarihi:", nLineWidth, 18);
            String fiyatDegisiklikTarihi = getFormattedDate(labelDto.getFiyatDegTarihi().substring(0, 11));
            printer.printAndroidFont(140, 375, Typeface.DEFAULT, true, false, fiyatDegisiklikTarihi, nLineWidth, 18);
            printer.printAndroidFont(115, 330, Typeface.DEFAULT, true, false, "Menşei: ", nLineWidth, 18);
            printer.printAndroidFont(115, 350, Typeface.DEFAULT, true, false, labelDto.getMensei(), nLineWidth, 18);


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
