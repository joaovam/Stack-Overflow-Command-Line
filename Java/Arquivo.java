import java.io.RandomAccessFile;
import java.lang.reflect.Constructor;
import java.util.Scanner;
import TabelaHashExtensivel.aed3.*;


public class Arquivo<T extends Registro, A extends RegistroHashExtensivel<A> > {

    Constructor<T> construtor;
    RandomAccessFile arq;
    Scanner sc;
    String nomeArquivo;
    HashExtensivel<A> he;
    Constructor<A> pcv;
    
    

    public Arquivo(Constructor<T> classe,Constructor<A> pcv, HashExtensivel<A> he ,String nome)throws Exception{


        this.he = he;
        this.pcv = pcv;
        this.nomeArquivo = nome;
        this.construtor = classe;
        arq = new RandomAccessFile(nomeArquivo,"rw");
        sc = new Scanner(System.in);
        if(arq.length() == 0){
            
            arq.writeInt(-1);
        }
        
    }
    public void escreveReg(int id,int tam,byte[] b)throws Exception{
        arq.writeBoolean(true);
        arq.writeInt(id);
        arq.writeInt(tam);
        arq.write(b);
    }

    public int create(T obj)throws Exception{
        arq.seek(0);
       
        int id = arq.readInt();
        id++;

        arq.seek(0);

        obj.setId(id);
        arq.writeInt(id);

        arq.seek(arq.length());

        byte[] b = obj.toByteArray();

        //Escreve registro completo ao final
        long pos = arq.getFilePointer();
        escreveReg(id, b.length , b);
        
        he.create(this.pcv.newInstance(pos,obj.getId()));
        
        

        return id;
    }
    
    public T read(int id)throws Exception{
        arq.seek(0);
        int max = arq.readInt();
        T obj = null;
        

        if(max >= id ){//confere se id nao extrapola 

            A indice =  he.read(id);//busca no indice
            if(indice!=null){
               arq.seek(indice.pos());
               if(arq.readBoolean()){

                  if(arq.readInt() ==id){
                     byte[] b = new byte[arq.readInt()];

                     arq.read(b);
                     obj = this.construtor.newInstance();
                     obj.fromByteArray(b);

                  }
               }
            }
         }
        return obj;
    }

   public boolean delete(int id)throws Exception{

        
        boolean deletou = false;
        
            arq.seek(0);
        
            if(arq.readInt()<=id){//confere se id extrapola nosso maior id sequencial, usado em todas as funções
               
               A indice = he.read(id);//le do indice,

               if(indice!=null){

                  arq.seek(indice.pos());//vai para a posição indicada no indice

                    if(arq.readBoolean()){//teste duplo

                       if(arq.readInt()==id){//teste triplo

                        arq.seek(indice.pos());
                        arq.writeBoolean(false);
                        he.delete(id);
                        deletou = true;

                       }
                    }   
                }
            }else{
               System.out.println("ID não cadastrado!");
           }
        
        return deletou;
   }

    public boolean update(T obj) throws Exception{

        arq.seek(0);

        boolean alterado = false;
        byte[] b = obj.toByteArray();
        
        if(obj.getId() > arq.readInt()){

            System.out.println("ID não cadastrado");
                
        }else{
            A indice = he.read(obj.getId());
            if(indice!=null){

               arq.seek(indice.pos());

               if(arq.readBoolean()){

                  if(arq.readInt() == obj.getId()){


                     if(arq.readInt() >= b.length){//trata se o espaco do registro for maior ou igual o resgistro atual

                        arq.write(b);

                     }else{//caso nao seja precisamos apagar e escrever ao final

                        arq.seek(indice.pos());//vai para posicao da lapide
                        arq.writeBoolean(false);//marca como apagada
                        arq.seek(arq.length());
                        long pos = arq.getFilePointer();
                        //Escreve novo registro completo
                        escreveReg(obj.getId(), b.length , b);
                        
                        he.update(pcv.newInstance(pos,obj.getId()));
                     }
                     alterado = true;
                  }
               }
            }   
        }
        return alterado;
    }


}
