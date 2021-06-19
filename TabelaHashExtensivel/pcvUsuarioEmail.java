
package TabelaHashExtensivel;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import TabelaHashExtensivel.aed3.RegistroHashExtensivel;


public class pcvUsuarioEmail implements RegistroHashExtensivel<pcvUsuarioEmail> {

  private int id; 
  private String email;
  private short TAMANHO = 44;      

  public pcvUsuarioEmail() {
    this("",-1 );
  }

  public pcvUsuarioEmail(String email, int id) {
    try {
      this.email = email;
      this.id = id;
      
    } catch (Exception ec) {
      ec.printStackTrace();
    }
  }
  public void setEmail(String email) {
      this.email = email;
  }
  public String getEmail() {
      return email;
  }
  public int getId(){
    return id;
  }

  @Override
  
  public int hashCode(){
    return this.email.hashCode();
  }
  public String email(){
    return this.email;
  }

  public short size(){
    return this.TAMANHO;
  }

  public String toString() {
    return this.email + " ; " + this.id;
  }

  public byte[] toByteArray() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(baos);
    dos.writeInt(this.id);
    dos.writeUTF(this.email);
    
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
    this.email = dis.readUTF();
  }

@Override
public long pos() {
    return 0;
}


}

