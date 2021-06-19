/*

Esta classe representa um PAR CHAVE VALOR (PCV) 
para uma entidade Pessoa. Seu objetivo é representar
uma entrada de índice. 

Esse índice será secundário e indireto, baseado no
email de uma pessoa. Ao fazermos a busca por pessoa,
ele retornará o ID dessa pessoa, para que esse ID
possa ser buscado em um índice direto (que não é
apresentado neste projeto)

Um índice direto de ID precisaria ser criado por meio
de outra classe, cujos dados fossem um int para o ID
e um long para o endereço
 
Implementado pelo Prof. Marcos Kutova
v1.0 - 2021
 
*/

package TabelaHashExtensivel;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import TabelaHashExtensivel.aed3.RegistroHashExtensivel;


public class pcvUsuario implements RegistroHashExtensivel<pcvUsuario> {

  private long posicao; 
  private int id;
  private short TAMANHO = 12;      

  public pcvUsuario() {
    this(0, -1);
  }

  public pcvUsuario(long p, int i) {
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