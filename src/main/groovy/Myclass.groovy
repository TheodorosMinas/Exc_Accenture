import groovy.time.*
import groovy.io.FileType
import groovy.util.logging.Log

import java.util.logging.Logger


@Log
class Myclass {
     public static void main(String[] args){
         def ifextra
         def pattern
         def rstring
         File f1
         File f2
         File x
         def exectime = new Date()
         log.info("Execution begins at:" + exectime.toString())
         //get a valid  input directory
         println("Please enter input directory path :")
         f1=ValidDir()
         println("ok")


         //get a valid output directory
         println("Please enter output directory path :")
         f2=ValidDir()

         //get the pattern
         println("Please enter pattern you want to be replaced ")
         pattern = System.in.newReader().readLine()

         //get the string to replace pattern
         println("please enter string to replace pattern")
         rstring = System.in.newReader().readLine()

         //check if backup is needed and get it if it is
         while (1) {
             println("Would you like a file containing the modified files : y/n")
             ifextra=System.in.newReader().readLine();
             if(ifextra=='y' || ifextra=='n'){
                 break;
             }
             else {
                 println("please enter y or n")
             }
         }

         DoAllTheWork(f1,pattern,rstring,ifextra,f2)
         def execfinn = new Date()
         log.info("Execution complete at " + execfinn.toString())



     }



    //method that does all the hard work
    static void DoAllTheWork(dir,pattern,rstring,ifextra,outdir){
        def list=[]
        File x
        File k
        File newFile
        def ftext

        //check if list containig modified files is required
        if(ifextra=='y'){
            newFile= new File(outdir.path+"/"+"list.txt")
            if(newFile.exists()){
                newFile.delete()
            }
            newFile.createNewFile()
        }
        //loop through all contents of the directory and take appropriate action
        dir.eachFileRecurse (FileType.FILES) { file ->
            list << file
        }
        list.each {

            x=new File(it.path)

            //check if there is a subdirectory and repeat proccess for that too
            if(x.isDirectory()){
                log.info("File $it.path is a directory")
                DoAllTheWork(x,pattern,rstring)
            }
            //
            else if(x.getText().find(pattern)){
                log.info("Pattern found in file $it.path")
                if(ifextra=='y'){
                    //write file path in list
                    log.info("Writing file name in list")
                    newFile.withWriterAppend {
                        writer -> writer.write("$it.path\n")
                    }
                }
                //backup files to be changed
                CreateBackup(outdir.path,x.text,x.getName())


                log.info("Replacing pattern with input String")
                //replace with new string
                ftext=x.text.replaceAll(pattern,rstring)
                x.write(ftext)

            }
        }
    }





    //get a valid directory path from keyboard
    // you can use src/main/resources/TheDir  src/main/resources/OutDir and that already exist or another valid one
    static File ValidDir(){
        def dir
        def path
        while (1) {
            path = System.in.newReader().readLine()
            dir = new File(path)
            //check if the path leads to a directory
            if (!dir.isDirectory()) {
                println("Invalid Directory please reenter")
                continue;
            }
            else{
                break;
            }
        }
        return dir;
    }

    //create back up files in output directory
    static void CreateBackup(outd_path,bfile_text,fname){
        File backup


        backup  = new File( "$outd_path" +"/" + "$fname")
        backup.createNewFile()
        backup.write(bfile_text)
    }



}