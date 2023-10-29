package com.hjq.language;

import java.util.Locale;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/MultiLanguages
 *    time   : 2022/04/08
 *    desc   : 语种契约类
 *    doc    : https://blog.csdn.net/liuhhaiffeng/article/details/54706027
 */
public final class LocaleContract {

   /** 中文 */
   private static volatile Locale sChineseLocale;

   public static Locale getChineseLocale() {
      if (sChineseLocale == null) {
         sChineseLocale = Locale.CHINESE;
      }
      return sChineseLocale;
   }

   /** 简体中文 */
   private static volatile Locale sSimplifiedChineseLocale;

   public static Locale getSimplifiedChineseLocale() {
      if (sSimplifiedChineseLocale == null) {
         sSimplifiedChineseLocale = Locale.SIMPLIFIED_CHINESE;
      }
      return sSimplifiedChineseLocale;
   }

   /** 繁体中文 */
   private static volatile Locale sTraditionalChineseLocale;

   public static Locale getTraditionalChineseLocale() {
      if (sTraditionalChineseLocale == null) {
         sTraditionalChineseLocale = Locale.TRADITIONAL_CHINESE;
      }
      return sTraditionalChineseLocale;
   }

   public static Locale getTaiWanLocale() {
      return getTraditionalChineseLocale();
   }

   /** 新加坡 */
   private static volatile Locale sSingaporeLocale ;

   public static Locale getSingaporeLocale() {
      if (sSimplifiedChineseLocale == null) {
         sSingaporeLocale = new Locale("zh", "SG");
      }
      return sSingaporeLocale;
   }

   /** 英语 */
   private static volatile Locale sEnglishLocale;

   public static Locale getEnglishLocale() {
      if (sEnglishLocale == null) {
         sEnglishLocale = Locale.ENGLISH;
      }
      return sEnglishLocale;
   }

   /** 法语 */
   private static volatile Locale sFrenchLocale;

   public static Locale getFrenchLocale() {
      if (sFrenchLocale == null) {
         sFrenchLocale = Locale.FRENCH;
      }
      return sFrenchLocale;
   }

   /** 德语 */
   private static volatile Locale sGermanLocale;

   public static Locale getGermanLocale() {
      if (sGermanLocale == null) {
         sGermanLocale = Locale.GERMAN;
      }
      return sGermanLocale;
   }

   /** 意大利语 */
   private static volatile Locale sItalianLocale;

   public static Locale getItalianLocale() {
      if (sItalianLocale == null) {
         sItalianLocale = Locale.ITALIAN;
      }
      return sItalianLocale;
   }

   /** 日语 */
   private static volatile Locale sJapaneseLocale;

   public static Locale getJapaneseLocale() {
      if (sJapaneseLocale == null) {
         sJapaneseLocale = Locale.JAPANESE;
      }
      return sJapaneseLocale;
   }

   /** 韩语 */
   private static volatile Locale sKoreanLocale;

   public static Locale getKoreanLocale() {
      if (sKoreanLocale == null) {
         sKoreanLocale = Locale.KOREAN;
      }
      return sKoreanLocale;
   }

   /** 越南语 */
   private static volatile Locale sVietnameseLocale;

   public static Locale getVietnameseLocale() {
      if (sVietnameseLocale == null) {
         sVietnameseLocale = new Locale("vi");
      }
      return sVietnameseLocale;
   }

   /** 荷兰语 */
   private static volatile Locale sDutchLocale;

   public static Locale getDutchLocale() {
      if (sDutchLocale == null) {
         sDutchLocale = new Locale("af");
      }
      return sDutchLocale;
   }

   /** 阿尔巴尼亚语 */
   private static volatile Locale sAlbanianLocale;

   public static Locale getAlbanianLocale() {
      if (sAlbanianLocale == null) {
         sAlbanianLocale = new Locale("sq");
      }
      return sAlbanianLocale;
   }

   /** 阿拉伯语 */
   private static volatile Locale sArabicLocale;

   public static Locale getArabicLocale() {
      if (sArabicLocale == null) {
         sArabicLocale = new Locale("ar");
      }
      return sArabicLocale;
   }

   /** 亚美尼亚语 */
   private static volatile Locale sArmenianLocale;

   public static Locale getArmenianLocale() {
      if (sArmenianLocale == null) {
         sArmenianLocale = new Locale("hy");
      }
      return sArmenianLocale;
   }

   /** 阿塞拜疆语 */
   private static volatile Locale sAzerbaijaniLocale;

   public static Locale getAzerbaijaniLocale() {
      if (sAzerbaijaniLocale == null) {
         sAzerbaijaniLocale = new Locale("az");
      }
      return sAzerbaijaniLocale;
   }

   /** 巴斯克语 */
   private static volatile Locale sBasqueLocale;

   public static Locale getBasqueLocale() {
      if (sBasqueLocale == null) {
         sBasqueLocale = new Locale("eu");
      }
      return sBasqueLocale;
   }

   /** 白俄罗斯 */
   private static volatile Locale sBelarusianLocale;

   public static Locale getBelarusianLocale() {
      if (sBelarusianLocale == null) {
         sBelarusianLocale = new Locale("be");
      }
      return sBelarusianLocale;
   }

   /** 保加利亚 */
   private static volatile Locale sBulgariaLocale;

   public static Locale getBulgariaLocale() {
      if (sBulgariaLocale == null) {
         sBulgariaLocale = new Locale("bg");
      }
      return sBulgariaLocale;
   }

   /** 加泰罗尼亚 */
   private static volatile Locale sCatalonianLocale;

   public static Locale getCatalonianLocale() {
      if (sCatalonianLocale == null) {
         sCatalonianLocale = new Locale("ca");
      }
      return sCatalonianLocale;
   }

   /** 克罗埃西亚 */
   private static volatile Locale sCroatiaLocale;

   public static Locale getCroatiaLocale() {
      if (sCroatiaLocale == null) {
         sCroatiaLocale = new Locale("hr");
      }
      return sCroatiaLocale;
   }

   /** 捷克 */
   private static volatile Locale sCzechRepublicLocale;

   public static Locale getCzechRepublicLocale() {
      if (sCzechRepublicLocale == null) {
         sCzechRepublicLocale = new Locale("cs");
      }
      return sCzechRepublicLocale;
   }

   /** 丹麦文 */
   private static volatile Locale sDanishLocale;

   public static Locale getDanishLocale() {
      if (sDanishLocale == null) {
         sDanishLocale = new Locale("da");
      }
      return sDanishLocale;
   }

   /** 迪维希语 */
   private static volatile Locale sDhivehiLocale;

   public static Locale getDhivehiLocale() {
      if (sDhivehiLocale == null) {
         sDhivehiLocale = new Locale("div");
      }
      return sDhivehiLocale;
   }

   /** 荷兰 */
   private static volatile Locale sNetherlandsLocale;

   public static Locale getNetherlandsLocale() {
      if (sNetherlandsLocale == null) {
         sNetherlandsLocale = new Locale("nl");
      }
      return sNetherlandsLocale;
   }

   /** 法罗语 */
   private static volatile Locale sFaroeseLocale;

   public static Locale getFaroeseLocale() {
      if (sFaroeseLocale == null) {
         sFaroeseLocale = new Locale("fo");
      }
      return sFaroeseLocale;
   }

   /** 爱沙尼亚 */
   private static volatile Locale sEstoniaLocale;

   public static Locale getEstoniaLocale() {
      if (sEstoniaLocale == null) {
         sEstoniaLocale = new Locale("et");
      }
      return sEstoniaLocale;
   }

   /** 波斯语 */
   private static volatile Locale sFarsiLocale;

   public static Locale getFarsiLocale() {
      if (sFarsiLocale == null) {
         sFarsiLocale = new Locale("fa");
      }
      return sFarsiLocale;
   }

   /** 芬兰语 */
   private static volatile Locale sFinnishLocale;

   public static Locale getFinnishLocale() {
      if (sFinnishLocale == null) {
         sFinnishLocale = new Locale("fi");
      }
      return sFinnishLocale;
   }

   /** 加利西亚 */
   private static volatile Locale sGaliciaLocale;

   public static Locale getGaliciaLocale() {
      if (sGaliciaLocale == null) {
         sGaliciaLocale = new Locale("gl");
      }
      return sGaliciaLocale;
   }

   /** 格鲁吉亚州 */
   private static volatile Locale sGeorgiaLocale;

   public static Locale getGeorgiaLocale() {
      if (sGeorgiaLocale == null) {
         sGeorgiaLocale = new Locale("ka");
      }
      return sGeorgiaLocale;
   }

   /** 希腊 */
   private static volatile Locale sGreeceLocale;

   public static Locale getGreeceLocale() {
      if (sGreeceLocale == null) {
         sGreeceLocale = new Locale("el");
      }
      return sGreeceLocale;
   }

   /** 古吉拉特语 */
   private static volatile Locale sGujaratiLocale;

   public static Locale getGujaratiLocale() {
      if (sGujaratiLocale == null) {
         sGujaratiLocale = new Locale("gu");
      }
      return sGujaratiLocale;
   }

   /** 希伯来 */
   private static volatile Locale sHebrewLocale;

   public static Locale getHebrewLocale() {
      if (sHebrewLocale == null) {
         sHebrewLocale = new Locale("he");
      }
      return sHebrewLocale;
   }

   /** 北印度语 */
   private static volatile Locale sHindiLocale;

   public static Locale getHindiLocale() {
      if (sHindiLocale == null) {
         sHindiLocale = new Locale("hi");
      }
      return sHindiLocale;
   }

   /** 匈牙利 */
   private static volatile Locale sHungaryLocale;

   public static Locale getHungaryLocale() {
      if (sHungaryLocale == null) {
         sHungaryLocale = new Locale("hu");
      }
      return sHungaryLocale;
   }

   /** 冰岛语 */
   private static volatile Locale sIcelandicLocale;

   public static Locale getIcelandicLocale() {
      if (sIcelandicLocale == null) {
         sIcelandicLocale = new Locale("is");
      }
      return sIcelandicLocale;
   }

   /** 印尼 */
   private static volatile Locale sIndonesiaLocale;

   public static Locale getIndonesiaLocale() {
      if (sIndonesiaLocale == null) {
         sIndonesiaLocale = new Locale("id");
      }
      return sIndonesiaLocale;
   }

   /** 卡纳达语 */
   private static volatile Locale sKannadaLocale;

   public static Locale getKannadaLocale() {
      if (sKannadaLocale == null) {
         sKannadaLocale = new Locale("kn");
      }
      return sKannadaLocale;
   }

   /** 哈萨克语 */
   private static volatile Locale sKazakhLocale;

   public static Locale getKazakhLocale() {
      if (sKazakhLocale == null) {
         sKazakhLocale = new Locale("kk");
      }
      return sKazakhLocale;
   }

   /** 贡根文 */
   private static volatile Locale sKonkaniLocale;

   public static Locale getKonkaniLocale() {
      if (sKonkaniLocale == null) {
         sKonkaniLocale = new Locale("kok");
      }
      return sKonkaniLocale;
   }

   /** 吉尔吉斯斯坦 */
   private static volatile Locale sKyrgyzLocale;

   public static Locale getKyrgyzLocale() {
      if (sKyrgyzLocale == null) {
         sKyrgyzLocale = new Locale("ky");
      }
      return sKyrgyzLocale;
   }

   /** 拉脱维亚 */
   private static volatile Locale sLatviaLocale;

   public static Locale getLatviaLocale() {
      if (sLatviaLocale == null) {
         sLatviaLocale = new Locale("lv");
      }
      return sLatviaLocale;
   }

   /** 立陶宛 */
   private static volatile Locale sLithuaniaLocale;

   public static Locale getLithuaniaLocale() {
      if (sLithuaniaLocale == null) {
         sLithuaniaLocale = new Locale("lt");
      }
      return sLithuaniaLocale;
   }

   /** 马其顿 */
   private static volatile Locale sMacedoniaLocale;

   public static Locale getMacedoniaLocale() {
      if (sMacedoniaLocale == null) {
         sMacedoniaLocale = new Locale("mk");
      }
      return sMacedoniaLocale;
   }

   /** 马来 */
   private static volatile Locale sMalayLocale;

   public static Locale getMalayLocale() {
      if (sMalayLocale == null) {
         sMalayLocale = new Locale("ms");
      }
      return sMalayLocale;
   }

   /** 马拉地语 */
   private static volatile Locale sMarathiLocale;

   public static Locale getMarathiLocale() {
      if (sMarathiLocale == null) {
         sMarathiLocale = new Locale("mr");
      }
      return sMarathiLocale;
   }

   /** 蒙古 */
   private static volatile Locale sMongoliaLocale;

   public static Locale getMongoliaLocale() {
      if (sMongoliaLocale == null) {
         sMongoliaLocale = new Locale("mn");
      }
      return sMongoliaLocale;
   }

   /** 挪威 */
   private static volatile Locale sNorwayLocale;

   public static Locale getNorwayLocale() {
      if (sNorwayLocale == null) {
         sNorwayLocale = new Locale("no");
      }
      return sNorwayLocale;
   }

   /** 波兰 */
   private static volatile Locale sPolandLocale;

   public static Locale getPolandLocale() {
      if (sPolandLocale == null) {
         sPolandLocale = new Locale("pl");
      }
      return sPolandLocale;
   }

   /** 葡萄牙语 */
   private static volatile Locale sPortugueseLocale;

   public static Locale getPortugalLocale() {
      if (sPortugueseLocale == null) {
         sPortugueseLocale = new Locale("pt");
      }
      return sPortugueseLocale;
   }

   /** 旁遮普语（印度和巴基斯坦语） */
   private static volatile Locale sPunjabLocale;

   public static Locale getPunjabLocale() {
      if (sPunjabLocale == null) {
         sPunjabLocale = new Locale("pa");
      }
      return sPunjabLocale;
   }

   /** 罗马尼亚语 */
   private static volatile Locale sRomanianLocale;

   public static Locale getRomanianLocale() {
      if (sRomanianLocale == null) {
         sRomanianLocale = new Locale("ro");
      }
      return sRomanianLocale;
   }

   /** 俄国 */
   private static volatile Locale sRussiaLocale;

   public static Locale getRussiaLocale() {
      if (sRussiaLocale == null) {
         sRussiaLocale = new Locale("ru");
      }
      return sRussiaLocale;
   }

   /** 梵文 */
   private static volatile Locale sSanskritLocale;

   public static Locale getSanskritLocale() {
      if (sSanskritLocale == null) {
         sSanskritLocale = new Locale("sa");
      }
      return sSanskritLocale;
   }

   /** 斯洛伐克 */
   private static volatile Locale sSlovakiaLocale;

   public static Locale getSlovakiaLocale() {
      if (sSlovakiaLocale == null) {
         sSlovakiaLocale = new Locale("sk");
      }
      return sSlovakiaLocale;
   }

   /** 斯洛文尼亚 */
   private static volatile Locale sSloveniaLocale;

   public static Locale getSloveniaLocale() {
      if (sSloveniaLocale == null) {
         sSloveniaLocale = new Locale("sl");
      }
      return sSloveniaLocale;
   }

   /** 西班牙语 */
   private static volatile Locale sSpanishLocale;

   public static Locale getSpainLocale() {
      if (sSpanishLocale == null) {
         sSpanishLocale = new Locale("es");
      }
      return sSpanishLocale;
   }

   /** 斯瓦希里语 */
   private static volatile Locale sSwahiliLocale;

   public static Locale getSwahiliLocale() {
      if (sSwahiliLocale == null) {
         sSwahiliLocale = new Locale("sw");
      }
      return sSwahiliLocale;
   }

   /** 瑞典 */
   private static volatile Locale sSwedenLocale;

   public static Locale getSwedenLocale() {
      if (sSwedenLocale == null) {
         sSwedenLocale = new Locale("sv");
      }
      return sSwedenLocale;
   }

   /** 叙利亚语 */
   private static volatile Locale sSyriacLocale;

   public static Locale getSyriacLocale() {
      if (sSyriacLocale == null) {
         sSyriacLocale = new Locale("syr");
      }
      return sSyriacLocale;
   }

   /** 坦米尔 */
   private static volatile Locale sTamilLocale;

   public static Locale getTamilLocale() {
      if (sTamilLocale == null) {
         sTamilLocale = new Locale("ta");
      }
      return sTamilLocale;
   }

   /** 泰国 */
   private static volatile Locale sThailandLocale;

   public static Locale getThailandLocale() {
      if (sThailandLocale == null) {
         sThailandLocale = new Locale("th");
      }
      return sThailandLocale;
   }

   /** 鞑靼语 */
   private static volatile Locale sTatarLocale;

   public static Locale getTatarLocale() {
      if (sTatarLocale == null) {
         sTatarLocale = new Locale("tt");
      }
      return sTatarLocale;
   }

   /** 泰卢固语 */
   private static volatile Locale sTeluguLocale;

   public static Locale getTeluguLocale() {
      if (sTeluguLocale == null) {
         sTeluguLocale = new Locale("te");
      }
      return sTeluguLocale;
   }

   /** 土耳其语 */
   private static volatile Locale sTurkishLocale;

   public static Locale getTurkishLocale() {
      if (sTurkishLocale == null) {
         sTurkishLocale = new Locale("tr");
      }
      return sTurkishLocale;
   }

   /** 乌克兰 */
   private static volatile Locale sUkraineLocale;

   public static Locale getUkraineLocale() {
      if (sUkraineLocale == null) {
         sUkraineLocale = new Locale("uk");
      }
      return sUkraineLocale;
   }

   /** 乌尔都语 */
   private static volatile Locale sUrduLocale;

   public static Locale getUrduLocale() {
      if (sUrduLocale == null) {
         sUrduLocale = new Locale("ur");
      }
      return sUrduLocale;
   }

   /** 乌兹别克语 */
   private static volatile Locale sUzbekLocale;

   public static Locale getUzbekLocale() {
      if (sUzbekLocale == null) {
         sUzbekLocale = new Locale("uz");
      }
      return sUzbekLocale;
   }
}