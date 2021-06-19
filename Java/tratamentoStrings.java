import java.text.Normalizer;
import java.util.regex.Pattern;

public class tratamentoStrings{

  static private String removerAcentos(String str){
    String normalizada = Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
    return pattern.matcher(normalizada).replaceAll(""); 
  }
  static public String formatarString(String str){

    return removerAcentos(str.trim().toLowerCase());
  
  }

  static public String[] separarStrings(String str){
    return str.split(";");
  }
  
  

  
}