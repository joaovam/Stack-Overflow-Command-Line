package TabelaHashExtensivel;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import TabelaHashExtensivel.aed3.RegistroHashExtensivel;


public class pcvResposta implements RegistroHashExtensivel<pcvResposta> {

  private long posicao; 
  private int id;
  private short TAMANHO = 12;      

  public pcvResposta() {

    this(0,-1);

  }

  public pcvResposta(long p, int i) {
    try {
      this.posicao = p;
      this.id = i;
      
    } catch (Exception ec) {
      ec.printStackTrace();
    }
  }
  public void setPosicao(long posicao) {
      this.posicao = posicao;
  }
  public long getPosicao() {
      return posicao;
  }

  @Override
  
  public int hashCode(){
    return this.id;
  }
  public long pos(){
    return posicao;
  }

  public short size(){
    return this.TAMANHO;
  }

  public String toString() {
    return this.posicao + " ; " + this.id;
  }

  public byte[] toByteArray() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(baos);
    dos.writeInt(this.id);
    dos.writeLong(this.posicao);
    
    byte[] bs = baos.toByteArray();
    byte[] bs2 = new byte[TAMANHO];
    for (int i = 0; i < TAMANHO; i++)
      bs2[i] = ' ';
    for (int i = 0; i < bs.length && i < TAMANHO; i++)
      bs2[i] = bs[i];
    return bs2;
  }

  public void fromByteArray(byte[] ba) throws IOException {
    ByteArrayInputStream bais = new ByteArrayInputStream(ba);
    DataInputStream dis = new DataInputStream(bais);
    this.id = dis.readInt();
    this.posicao = dis.readLong();
  }
}