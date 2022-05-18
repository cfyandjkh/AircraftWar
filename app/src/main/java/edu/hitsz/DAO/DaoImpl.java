package edu.hitsz.DAO;

import edu.hitsz.valueobject.Objectvalue;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class DaoImpl implements Dao {
    private  File file;
    private  List<Objectvalue> list;
    private  List<String> stringList;
    /**
     * 日期处理
     */

    public DaoImpl(String pathname) throws IOException {
        file = new File(pathname);
        list = new ArrayList<>();
        stringList = new ArrayList<>();
    }

    @Override
    public void read() throws FileNotFoundException {
        BufferedReader reader=new BufferedReader(new FileReader(file));
        try {

            String read;
            while ((read = reader.readLine()) != null) {
                stringList.add(read);
            }
            String[] stringSeperate;
            for (String s : stringList) {
                stringSeperate = s.split("\\s+");
                list.add(new Objectvalue(stringSeperate[1], Integer.parseInt(stringSeperate[2]),stringSeperate[3]+" "+stringSeperate[4]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void write(Objectvalue objectvalue) throws IOException {
        /**
         * filewriter默认为false，进行重写
         * 注意覆盖是在实例化之后就立马对文件进行了清空操作，并不是在写的时候才清空，因此要先读完再实例化bufferedwriter。
         */
        BufferedWriter writer =new BufferedWriter(new FileWriter(file));
        if(objectvalue!=null){
            list.add(objectvalue);
        }
        list.sort(Objectvalue.Comparators.SCORE);
        try {
            System.out.println("******************************");
            String name, score, place, s;
            for (int i = 0; i < list.size(); i++) {
                name = list.get(i).getName();
                score = Integer.toString(list.get(i).getScore());
                place = Integer.toString(i + 1);
                s = "第" + place + "名 " + name + " " + score + " " + list.get(i).getTime();
                writer.write(s);
                writer.newLine();
                System.out.println(s);
            }
            writer.flush();
            System.out.println("******************************");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Objectvalue> getList(){
        return list;
    };
    public  void setList(List<Objectvalue> list){
        this.list=list;
    }
}
