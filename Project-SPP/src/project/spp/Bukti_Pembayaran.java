/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.spp;

import Config.koneksiDB;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.text.DateFormat;
import java.util.Date;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author Sammy Guergachi <sguergachi at gmail.com>
 */
public class Bukti_Pembayaran extends javax.swing.JFrame {

    /**
     * Creates new form Setting_SPP
     */
    public Bukti_Pembayaran() {
        initComponents();
        load_table();
        width_column();
        tampil_kelas();
        tampil_tahun();
        tampil_bulan_terpilih();
        btnBayar.setEnabled(false);
        btnCetak.setEnabled(false);
        btnCancel.setEnabled(false);
        
        txtIdKelas.setVisible(true);
    }
    
    private void kosong(){
        txtNisn.setText("");
        txtNis.setText("");
        txtNamaSiswa.setText("");
        cbKelas.setSelectedItem("-Pilih Kelas-");
        cbBulan.setSelectedItem("-Pilih Tahun SPP-");
        txtNominal.setText("Rp. ");
    }
    
    private void load_table(){        
        // membuat tampilan model tabel
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("No.");
        model.addColumn("ID Trans.");
        model.addColumn("Nama Siswa");
        model.addColumn("Kelas");
        model.addColumn("Bulan Bayar");
        model.addColumn("Tahun Bayar");
        model.addColumn("SPP");
        
        //menampilkan data database kedalam tabel
        try {
            int no=1;
            
            //Buat variabel untuk mengambil tanggal hari ini
            Date currentDate = new Date();
            
            String sql = "SELECT * FROM pembayaran pb, siswa sw, kelas kl, spp sp, jurusan jr WHERE "
                    + "pb.nisn=sw.nisn AND sw.id_kelas=kl.id_kelas AND kl.id_jurusan=jr.id_jurusan AND sw.id_spp=sp.id_spp ORDER BY pb.id_pembayaran DESC";
            java.sql.Connection conn=(Connection)koneksiDB.connectDB();
            java.sql.Statement stm=conn.createStatement();
            java.sql.ResultSet res=stm.executeQuery(sql);
            while(res.next()){
                String id_pembayaran = res.getString("id_pembayaran");
                String nisn = res.getString("nisn");
                String nis = res.getString("nis"); 
                String nama_siswa = res.getString("nama_siswa"); 
                String nama_kelas = res.getString("nama_kelas");
                String jurusan = res.getString("jurusan");
                String bulan = res.getString("bulan_bayar");
                String tahun_bayar = res.getString("tahun_bayar");
                String tgl_bayar = res.getString("tgl_bayar");
                
                //mengambil Tahun Angkatan dari DB
                String tahun = res.getString("tahun_angkatan");
                //menjadikan tahun angkatan sesuai dengan format sekolah 1-Juli-[tahun_angkatan]
                String tahun_terhitung = "01-07-"+tahun;
                
                //Selanjutnya perlu untuk mengubah format tanggal dari String ke tipe Date, 
                //gunakan SimpleDateFormat untuk memformat tanggal yang sebelumnya ditulis dalam bentuk String
                DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                
                //Selanjutnya lakukan parsing tanggal dari format String menjadi Date
                Date tahunMasuk = format.parse(tahun_terhitung);
                
                //konversi dari tipe data Date ke tipe data Long
                long tahunMasuk1 = tahunMasuk.getTime(); 
                long tahunSekarang1 = currentDate.getTime(); 
                
                //Setelah tanggal berhasil diparsing ke tipe Date, 
                //dapat dicari selisih waktunya dari tanggal hari ini dalam satuan miliseconds
                long diff = tahunSekarang1 - tahunMasuk1;
                //mencari selisih dengan format hitungan Bulan
                long lama = (diff / (24 * 60 * 60 * 1000))/30;
                String tingkat = Long.toString(lama);
                
                int tingkat_int;
                tingkat_int = Integer.parseInt(tingkat);
                String kelas="";
                if (tingkat_int <= 12) {
                    kelas = "X "+ nama_kelas;
                } else if (tingkat_int <= 24) {
                    kelas = "XI "+ nama_kelas;
                } else if(tingkat_int <= 36) {
                    kelas = "XII "+ nama_kelas;
                } else if(tingkat_int > 36) {
                    kelas = "Alumni "+ nama_kelas;
                }                
                
                //mengatur menjadi penyebutan dalam Rupiah
                Double y = Double.parseDouble(res.getString("nominal"));
                NumberFormat nf = NumberFormat.getCurrencyInstance();
                String nominal = nf.format(y);
                
                model.addRow(new Object[]{no++, id_pembayaran, nama_siswa, kelas, bulan, tahun_bayar, nominal});
            }
            tblSiswa.setModel(model);
        } catch (Exception e) {
        }
    }
    
    public void width_column(){        
        TableColumn column;
        tblSiswa.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF); 
        //Nomor Urut
        column = tblSiswa.getColumnModel().getColumn(0); 
        column.setPreferredWidth(40);
        //ID Pembayaran
        column = tblSiswa.getColumnModel().getColumn(1); 
        column.setPreferredWidth(140); 
        //Nama Siswa
        column = tblSiswa.getColumnModel().getColumn(2); 
        column.setPreferredWidth(280); 
        //Tingkat
        column = tblSiswa.getColumnModel().getColumn(3); 
        column.setPreferredWidth(80);        
        //Bulan
        column = tblSiswa.getColumnModel().getColumn(4); 
        column.setPreferredWidth(30);      
        //Tahun
        column = tblSiswa.getColumnModel().getColumn(5); 
        column.setPreferredWidth(40);      
        //Nominal SPP
        column = tblSiswa.getColumnModel().getColumn(6); 
        column.setPreferredWidth(100);      
        //Kelas
        //column = tblKelas.getColumnModel().getColumn(6); 
        //column.setPreferredWidth(75);
    }   
    
    private void load_data_by_kelas(){
         // membuat tampilan model tabel
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("No.");
        model.addColumn("NISN.");
        model.addColumn("Nama Siswa");
        model.addColumn("Tingkat");
        model.addColumn("Jurusan");
        model.addColumn("SPP");
        
        //menampilkan data database kedalam tabel
        try {
            int no=1;
            
            //Buat variabel untuk mengambil tanggal hari ini
            Date currentDate = new Date();
            
            String sql = "SELECT * FROM siswa sw, kelas kl, spp sp, jurusan jr WHERE "
                    + "sw.id_kelas=kl.id_kelas AND kl.id_jurusan=jr.id_jurusan AND sw.id_spp=sp.id_spp "
                    + "AND sw.id_kelas = '" +txtIdKelas.getText()+ "' ORDER BY sw.nama_siswa ASC";
            java.sql.Connection conn=(Connection)koneksiDB.connectDB();
            java.sql.Statement stm=conn.createStatement();
            java.sql.ResultSet res=stm.executeQuery(sql);
            while(res.next()){
                String nisn = res.getString("nisn");
                String nis = res.getString("nis"); 
                String nama_siswa = res.getString("nama_siswa"); 
                String nama_kelas = res.getString("nama_kelas");
                String jurusan = res.getString("jurusan");
                
                //mengambil Tahun Angkatan dari DB
                String tahun = res.getString("tahun_angkatan");
                //menjadikan tahun angkatan sesuai dengan format sekolah 1-Juli-[tahun_angkatan]
                String tahun_terhitung = "01-07-"+tahun;
                
                //Selanjutnya perlu untuk mengubah format tanggal dari String ke tipe Date, 
                //gunakan SimpleDateFormat untuk memformat tanggal yang sebelumnya ditulis dalam bentuk String
                DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                
                //Selanjutnya lakukan parsing tanggal dari format String menjadi Date
                Date tahunMasuk = format.parse(tahun_terhitung);
                
                //konversi dari tipe data Date ke tipe data Long
                long tahunMasuk1 = tahunMasuk.getTime(); 
                long tahunSekarang1 = currentDate.getTime(); 
                
                //Setelah tanggal berhasil diparsing ke tipe Date, 
                //dapat dicari selisih waktunya dari tanggal hari ini dalam satuan miliseconds
                long diff = tahunSekarang1 - tahunMasuk1;
                //mencari selisih dengan format hitungan Bulan
                long lama = (diff / (24 * 60 * 60 * 1000))/30;
                String tingkat = Long.toString(lama);
                
                int tingkat_int;
                tingkat_int = Integer.parseInt(tingkat);
                String kelas="";
                if (tingkat_int <= 12) {
                    kelas = "X "+ nama_kelas;
                } else if (tingkat_int <= 24) {
                    kelas = "XI "+ nama_kelas;
                } else if(tingkat_int <= 36) {
                    kelas = "XII "+ nama_kelas;
                } else if(tingkat_int > 36) {
                    kelas = "Alumni "+ nama_kelas;
                }                
                
                //mengatur menjadi penyebutan dalam Rupiah
                Double y = Double.parseDouble(res.getString("nominal"));
                NumberFormat nf = NumberFormat.getCurrencyInstance();
                String nominal = nf.format(y);
                
                model.addRow(new Object[]{no++,nisn, nama_siswa, kelas, jurusan, nominal});
            }
            tblSiswa.setModel(model);
        } catch (Exception e) {
        }
    }
   
    /*private void date(){
        //mengambil tanggal hari ini
        SimpleDateFormat dateFormat =new SimpleDateFormat("dd-MM-yyyy");
        java.util.Date date = new java.util.Date();
        txtTgl.setText(dateFormat.format(date));
    }*/
        
    public void tampil_kelas(){
        
         try {
            int no=1;
            //Buat variabel untuk mengambil tanggal hari ini
            Date currentDate = new Date();
            
            String sql = "SELECT * FROM kelas ORDER BY kode_kelas DESC";
            java.sql.Connection conn=(Connection)koneksiDB.connectDB();
            java.sql.Statement stm=conn.createStatement();
            java.sql.ResultSet res=stm.executeQuery(sql);
            
            cbKelas.addItem("-Pilih Kelas-");
            while(res.next()){
                
                String nama = res.getString("nama_kelas"); 
                
                //mengambil Tahun Angkatan dari DB
                String tahun = res.getString("tahun_angkatan");
                //menjadikan tahun angkatan sesuai dengan format sekolah 1-Juli-[tahun_angkatan]
                String tahun_terhitung = "01-07-"+tahun;
                
                //Selanjutnya perlu untuk mengubah format tanggal dari String ke tipe Date, 
                //gunakan SimpleDateFormat untuk memformat tanggal yang sebelumnya ditulis dalam bentuk String
                DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                
                //Selanjutnya lakukan parsing tanggal dari format String menjadi Date
                Date tahunMasuk = format.parse(tahun_terhitung);
                
                //konversi dari tipe data Date ke tipe data Long
                long tahunMasuk1 = tahunMasuk.getTime(); 
                long tahunSekarang1 = currentDate.getTime(); 
                
                //Setelah tanggal berhasil diparsing ke tipe Date, 
                //dapat dicari selisih waktunya dari tanggal hari ini dalam satuan miliseconds
                long diff = tahunSekarang1 - tahunMasuk1;
                //mencari selisih dengan format hitungan Bulan
                long lama = (diff / (24 * 60 * 60 * 1000))/30;
                String tingkat = Long.toString(lama);
                
                int tingkat_int;
                tingkat_int = Integer.parseInt(tingkat);
                String kelas="";
                if (tingkat_int <= 12) {
                    kelas = "X "+ nama;
                } else if (tingkat_int <= 24) {
                    kelas = "XI "+ nama;
                } else if(tingkat_int <= 36) {
                    kelas = "XII "+ nama;
                } else if(tingkat_int > 36) {
                    kelas = "Alumni "+ nama;
                }
                cbKelas.addItem(kelas+"-"+(res.getString("id_kelas")));
            }
        }
        catch(Exception ex){
        }
    }
        
    public void tampil_id_kelas(){
        String kelas;
        kelas = (String) cbKelas.getSelectedItem();
        String[] kata = kelas.split("\\-");
        
        String idKelas = kata[1];
        
        txtIdKelas.setText(idKelas);
    }
    
    public void tampil_bulan_terpilih(){
        String bulan;
        bulan = (String) cbBulan.getSelectedItem();
        String[] kata = bulan.split("\\-");
        
        String idBulan = kata[0];
        
        txtIdBulan.setText(idBulan);
    }
    
    public void tampil_nominal(){
         try {
            int no=1;
            String sql = "SELECT nominal FROM spp WHERE id_spp= '" +cbBulan.getSelectedItem()+ "'";
            java.sql.Connection conn=(Connection)koneksiDB.connectDB();
            java.sql.Statement stm=conn.createStatement();
            java.sql.ResultSet res=stm.executeQuery(sql);
            
            while(res.next()){
                //mengatur menjadi penyebutan dalam Rupiah
                Double y = Double.parseDouble(res.getString("nominal"));
                NumberFormat nf = NumberFormat.getCurrencyInstance();
                String nominal = nf.format(y);
                txtNominal.setText(nominal);
            }
        }
        catch(Exception ex){
        }
    }
    
    private void simpan_spp(){
        
        SimpleDateFormat timeFormat =new SimpleDateFormat("yyMMddHHmmss");
        java.util.Date date = new java.util.Date();
        //Mendapatkan Tahun Sekarang
        String id_base_tahun = timeFormat.format(date);
        
        String nisn, id_petugas, tgl_bayar, bulan_bayar, tahun_bayar, id_spp, jumlah_bayar;
        nisn = txtNisn.getText();
        id_petugas = "3";
        tgl_bayar = txtTanggalBayar.getText();
        bulan_bayar = txtIdBulan.getText();
        tahun_bayar =(String) cbTahun.getSelectedItem();
        id_spp = txtIdSPP.getText();
        jumlah_bayar = txtNominal.getText();
       
            if (JOptionPane.showConfirmDialog(null, "Apakah Anda Yakin Ingin Menyimpan?", "Window Save", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

                try {

                    java.sql.Connection conn = (Connection) koneksiDB.connectDB();
                    //INPUT KE Table PEMINJAMAN
                    String sql = "INSERT INTO pembayaran (id_pembayaran, id_petugas, nisn, tgl_bayar, bulan_bayar, tahun_bayar, id_spp, jumlah_bayar)"
                                                      + "VALUES ('" + id_base_tahun + "','" + id_petugas + "','" + nisn + "','" + tgl_bayar + "','" + bulan_bayar + "',"
                            + "'" + tahun_bayar + "','" + id_spp + "','" + jumlah_bayar + "')";                                
                    java.sql.PreparedStatement pst = conn.prepareStatement(sql);
                    pst.execute();

                    JOptionPane.showMessageDialog(null, "Penyimpanan Data Berhasil");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, e.getMessage());
                }            
            load_table();            
            width_column();
            kosong();          
        }
    }
    
    public void edit_siswa(){
        String nisn, nis, nama_siswa, id_kelas, alamat, no_telp, id_spp;
        id_kelas = txtIdKelas.getText();
        nisn = txtNisn.getText();
        nis = txtNis.getText();
        nama_siswa = txtNamaSiswa.getText();
        id_spp = (String) cbBulan.getSelectedItem();
                
        //Konversi String ke Nilai Integer supaya dapat dibandingkan
        /*int jumlah_pinjam_int, stok_int, sisa_stok;
        jumlah_pinjam_int = Integer.parseInt(jumlah); //variabel Jumlah Barang dipinjam
        stok_int = Integer.parseInt(stok); //variabel jumlah stok barang di Database
        sisa_stok = stok_int - jumlah_pinjam_int;
                
        String tgl_pinjam = "yyyy-MM-dd";
        SimpleDateFormat fm = new SimpleDateFormat (tgl_pinjam);
        String tanggal_pinjam = String.valueOf(fm.format(tglPinjam.getDate()));
        */
       
            if (JOptionPane.showConfirmDialog(null, "Apakah Anda Yakin Ingin Menyimpan?", "Window Save", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

                try {

                    java.sql.Connection conn = (Connection) koneksiDB.connectDB();
                    //INPUT KE Table Siswa
                    String sql = "UPDATE siswa SET nis = '" + nis + "', nama_siswa = '" + nama_siswa + "', id_kelas = '" + id_kelas + "',"
                            + "id_spp = '" + id_spp + "' WHERE nisn = '" + nisn + "'";
                    java.sql.PreparedStatement pstb = conn.prepareStatement(sql);
                    pstb.execute();

                    JOptionPane.showMessageDialog(null, "Penyimpanan Data Berhasil");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, e.getMessage());
                }            
            load_table();            
            width_column();
            kosong();
            btnBayar.setEnabled(false);
            btnCetak.setEnabled(false);
            btnCancel.setEnabled(false);
        }
    }
    
    private void hapus_siswa(){
        String nisn;
        nisn = txtNisn.getText();
       
            if (JOptionPane.showConfirmDialog(null, "Apakah Anda Yakin Ingin Menghapus?", "Window Save", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

                try {

                    java.sql.Connection conn = (Connection) koneksiDB.connectDB();
                    //Delete data sesuai NISN
                    String sql = "DELETE FROM siswa WHERE nisn = '" + nisn + "'";
                    java.sql.PreparedStatement pstb = conn.prepareStatement(sql);
                    pstb.execute();

                    JOptionPane.showMessageDialog(null, "Data Berhasil Dihapus");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, e.getMessage());
                }            
            load_table();            
            width_column();
            kosong();
            btnBayar.setEnabled(false);
            btnCetak.setEnabled(false);
            btnCancel.setEnabled(false);
            txtNisn.setEnabled(true);
        }
    }
    
    private void tampil_tahun(){
        //Buat variabel untuk mengambil tahun saat ini
        SimpleDateFormat dateFormat =new SimpleDateFormat("yyyy");
        SimpleDateFormat tglFormat =new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date = new java.util.Date();
        //Mendapatkan Tahun Sekarang
        String tahunSekarang = dateFormat.format(date);
        //Mendapatkan Tgl-Bulan-Tahun Sekarang
        String tglSekarang = tglFormat.format(date);
        txtTanggalBayar.setText(tglSekarang);
        
        int tahunIni, batasTahun;
        //parsing tahun yang didapat menjadi Integer
        tahunIni = Integer.parseInt(tahunSekarang);
        
        //untuk mendapatkan batas 20 tahun kebelakang
        batasTahun = tahunIni - 20;
        
        //Looping untuk menampilkan urutan 20 tahun -> saat ini
            //cbTahun.addItem(tahunSekarang);
        for (int t=tahunIni+1; t >= batasTahun; t=t-1){
            //Kembalikan nilai Integer menjadi String
            String tahun = Integer.toString(t);
            cbTahun.addItem(tahun);
        }
        cbTahun.setSelectedItem(tahunSekarang);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtIdKelas = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtTanggalBayar = new javax.swing.JLabel();
        txtIdBulan = new javax.swing.JLabel();
        btnBayar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblSiswa = new javax.swing.JTable();
        jLabel11 = new javax.swing.JLabel();
        cbKelas = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        btnCetak = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtNominal = new javax.swing.JLabel();
        cbBulan = new javax.swing.JComboBox<>();
        cbTahun = new javax.swing.JComboBox<>();
        txtIdSPP = new javax.swing.JLabel();
        txtNamaSiswa = new javax.swing.JLabel();
        txtNisn = new javax.swing.JLabel();
        txtNis = new javax.swing.JLabel();
        tbiasa = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtNota = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 51));

        jLabel1.setFont(new java.awt.Font("Lato", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 51, 102));
        jLabel1.setText("Pembayaran SPP");

        txtIdKelas.setForeground(new java.awt.Color(0, 0, 0));
        txtIdKelas.setText("idKelas");

        jLabel9.setFont(new java.awt.Font("Lato", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 0, 0));
        jLabel9.setText("Tanggal : ");

        txtTanggalBayar.setFont(new java.awt.Font("Courier New", 1, 12)); // NOI18N
        txtTanggalBayar.setForeground(new java.awt.Color(153, 0, 0));
        txtTanggalBayar.setText("jLabel6");

        txtIdBulan.setForeground(new java.awt.Color(0, 0, 0));
        txtIdBulan.setText("jLabel6");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel1)
                .addGap(98, 98, 98)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTanggalBayar)
                .addGap(90, 90, 90)
                .addComponent(txtIdKelas)
                .addGap(18, 18, 18)
                .addComponent(txtIdBulan)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtIdKelas)
                        .addComponent(txtIdBulan))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(jLabel9)
                        .addComponent(txtTanggalBayar)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnBayar.setBackground(new java.awt.Color(51, 255, 51));
        btnBayar.setFont(new java.awt.Font("Lato", 1, 14)); // NOI18N
        btnBayar.setForeground(new java.awt.Color(255, 255, 255));
        btnBayar.setText("Bayar");
        btnBayar.setPreferredSize(new java.awt.Dimension(55, 33));
        btnBayar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBayarActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(204, 255, 153));
        jPanel2.setForeground(new java.awt.Color(204, 255, 153));

        tblSiswa.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tblSiswa.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblSiswa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSiswaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblSiswa);

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(51, 0, 51));
        jLabel11.setText("Kelas");

        cbKelas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbKelasActionPerformed(evt);
            }
        });
        cbKelas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cbKelasKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cbKelasKeyReleased(evt);
            }
        });

        jButton1.setText("Search");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cbKelas, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 721, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(56, 56, 56))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbKelas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jLabel5.setForeground(new java.awt.Color(255, 102, 51));
        jLabel5.setText("Ket: NISN tidak boleh kosong!");

        btnCetak.setBackground(new java.awt.Color(51, 153, 255));
        btnCetak.setFont(new java.awt.Font("Lato", 1, 14)); // NOI18N
        btnCetak.setForeground(new java.awt.Color(255, 255, 255));
        btnCetak.setText("Cetak");
        btnCetak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCetakActionPerformed(evt);
            }
        });

        btnCancel.setBackground(new java.awt.Color(255, 51, 51));
        btnCancel.setFont(new java.awt.Font("Lato", 1, 14)); // NOI18N
        btnCancel.setForeground(new java.awt.Color(255, 255, 255));
        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        jPanel3.setBackground(new java.awt.Color(204, 204, 255));
        jPanel3.setForeground(new java.awt.Color(255, 153, 204));

        jLabel2.setFont(new java.awt.Font("Lato", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("N I S N               :");

        jLabel3.setFont(new java.awt.Font("Lato", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("N I S                   :");

        jLabel4.setFont(new java.awt.Font("Lato", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("Nama Siswa    :");

        jLabel7.setFont(new java.awt.Font("Lato", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("Bulan Bayar   :");

        jLabel8.setFont(new java.awt.Font("Lato", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jLabel8.setText("Tahun Bayar  :");

        jLabel10.setFont(new java.awt.Font("Lato", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 0, 0));
        jLabel10.setText("Nominal           :");

        txtNominal.setFont(new java.awt.Font("Lato", 1, 14)); // NOI18N
        txtNominal.setForeground(new java.awt.Color(0, 0, 0));

        cbBulan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1-Januari", "2-Februari", "3-Maret", "4-April", "5-Mei", "6-Juni", "7-Juli", "8-Agustus", "9-September", "10-Oktober", "11-November", "12-Desember" }));
        cbBulan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbBulanActionPerformed(evt);
            }
        });

        txtIdSPP.setFont(new java.awt.Font("Lato", 1, 10)); // NOI18N
        txtIdSPP.setForeground(new java.awt.Color(0, 0, 0));
        txtIdSPP.setText("Id SPP");

        txtNamaSiswa.setFont(new java.awt.Font("Lato", 1, 14)); // NOI18N
        txtNamaSiswa.setForeground(new java.awt.Color(51, 0, 0));
        txtNamaSiswa.setText("Nama Siswa");

        txtNisn.setFont(new java.awt.Font("Lato", 1, 14)); // NOI18N
        txtNisn.setForeground(new java.awt.Color(51, 0, 0));
        txtNisn.setText("NISN");

        txtNis.setFont(new java.awt.Font("Lato", 1, 12)); // NOI18N
        txtNis.setForeground(new java.awt.Color(51, 0, 0));
        txtNis.setText("NIS Siswa");

        tbiasa.setFont(new java.awt.Font("Lato", 1, 14)); // NOI18N
        tbiasa.setForeground(new java.awt.Color(0, 0, 0));
        tbiasa.setText("Rp.");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                        .addComponent(cbBulan, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(136, 136, 136))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel10))
                                .addGap(20, 20, 20)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cbTahun, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(tbiasa)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtNominal)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtIdSPP)
                                        .addGap(35, 35, 35))))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addComponent(txtNisn, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtNis)
                                    .addComponent(txtNamaSiswa, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addContainerGap(14, Short.MAX_VALUE))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNisn))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtNis))
                .addGap(22, 22, 22)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtNamaSiswa))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(cbBulan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(cbTahun, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtNominal)
                    .addComponent(txtIdSPP)
                    .addComponent(tbiasa))
                .addContainerGap(30, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(255, 204, 153));

        txtNota.setColumns(20);
        txtNota.setFont(new java.awt.Font("Lato Black", 0, 14)); // NOI18N
        txtNota.setRows(5);
        jScrollPane2.setViewportView(txtNota);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 675, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(85, 85, 85)
                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnCetak, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnBayar, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 734, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(9, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCetak)
                    .addComponent(btnBayar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancel))
                .addGap(12, 12, 12))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBayarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBayarActionPerformed
        // TODO add your handling code here:
        simpan_spp();
    }//GEN-LAST:event_btnBayarActionPerformed

    private void cbKelasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbKelasActionPerformed
        // TODO add your handling code here:
        tampil_id_kelas();
    }//GEN-LAST:event_cbKelasActionPerformed

    private void tblSiswaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSiswaMouseClicked
        // TODO add your handling code here:
        int baris = tblSiswa.rowAtPoint(evt.getPoint());
        String id_pembayaran = tblSiswa.getValueAt(baris, 1).toString();
        
        try {
            //Buat variabel untuk mengambil tanggal hari ini
            Date currentDate = new Date();
            //int no=1;
            String sql = "SELECT * FROM pembayaran pb, siswa sw, spp sp, kelas kl, jurusan jr WHERE "
                        + "pb.nisn=sw.nisn AND sw.id_kelas=kl.id_kelas AND sw.id_spp=sp.id_spp AND sp.id_jurusan = jr.id_jurusan AND pb.id_pembayaran = '" + id_pembayaran + "'";
            java.sql.Connection conn=(Connection)koneksiDB.connectDB();
            java.sql.Statement stm=conn.createStatement();
            java.sql.ResultSet res=stm.executeQuery(sql);
            while(res.next()){
                String nisn = res.getString ("nisn");
                String nis = res.getString("nis");
                String nama_siswa = res.getString("nama_siswa");                
                String nama_kelas = res.getString("nama_kelas");
                String tahun = res.getString("tahun_angkatan");
                String alamat = res.getString("alamat");
                String no_telp = res.getString("no_tlp");
                String id_spp = res.getString("id_spp");
                String bulan_bayar = res.getString("bulan_bayar");
                String tahun_bayar = res.getString("tahun_bayar");
                
                //menjadikan tahun angkatan sesuai dengan format sekolah 1-Juli-[tahun_angkatan]
                String tahun_terhitung = "01-07-"+tahun;
                
                //Selanjutnya perlu untuk mengubah format tanggal dari String ke tipe Date, 
                //gunakan SimpleDateFormat untuk memformat tanggal yang sebelumnya ditulis dalam bentuk String
                DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                
                //Selanjutnya lakukan parsing tanggal dari format String menjadi Date
                Date tahunMasuk = format.parse(tahun_terhitung);
                
                //konversi dari tipe data Date ke tipe data Long
                long tahunMasuk1 = tahunMasuk.getTime(); 
                long tahunSekarang1 = currentDate.getTime(); 
                
                //Setelah tanggal berhasil diparsing ke tipe Date, 
                //dapat dicari selisih waktunya dari tanggal hari ini dalam satuan miliseconds
                long diff = tahunSekarang1 - tahunMasuk1;
                //mencari selisih dengan format hitungan Bulan
                long lama = (diff / (24 * 60 * 60 * 1000))/30;
                String tingkat = Long.toString(lama);
                
                int tingkat_int;
                tingkat_int = Integer.parseInt(tingkat);
                String kelas="";
                if (tingkat_int <= 12) {
                    kelas = "X "+ nama_kelas;
                } else if (tingkat_int <= 24) {
                    kelas = "XI "+ nama_kelas;
                } else if(tingkat_int <= 36) {
                    kelas = "XII "+ nama_kelas;
                } else if(tingkat_int > 36) {
                    kelas = "Alumni "+ nama_kelas;
                }
                cbKelas.setSelectedItem(kelas+"-"+(res.getString("id_kelas")));
                
                
                //mengatur menjadi penyebutan dalam Rupiah
                Double y = Double.parseDouble(res.getString("nominal"));
                NumberFormat nf = NumberFormat.getCurrencyInstance();
                String nominal = nf.format(y);
                
                
                
                txtIdSPP.setText(id_spp);
                txtNota.setText("                                                                      BUKTI PEMBAYARAN SPP               "
                              + "\n_______________________________________________________________________________________________________"
                              + "\n       No. Transaksi : " + id_pembayaran + "                NISN              :" + nisn
                              + "\n       NIS                       : " + nis + "                                      Nama Siswa : " + nama_siswa
                              + "\n       Kelas                   : " + kelas
                              + "\n_______________________________________________________________________________________________________"
                              + "\n"
                              + "\n       Pembayaran SPP Bulan " + bulan_bayar + " tahun " + tahun_bayar + ", sejumlah " + nominal);
                btnBayar.setEnabled(true);
                btnCetak.setEnabled(true);
                btnCancel.setEnabled(true);
                //cbSumberDana.setSelectedItem(sumber);
            }
        }catch (Exception e) {
        }
    }//GEN-LAST:event_tblSiswaMouseClicked

    private void btnCetakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCetakActionPerformed
        // TODO add your handling code here:
        edit_siswa();
    }//GEN-LAST:event_btnCetakActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        btnBayar.setEnabled(false);
        btnCetak.setEnabled(false);
        btnCancel.setEnabled(false);
        kosong();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void cbKelasKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbKelasKeyReleased
         // TODO add your handling code here:
       
    }//GEN-LAST:event_cbKelasKeyReleased

    private void cbKelasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbKelasKeyPressed
         // TODO add your handling code here:
           if (evt.getKeyCode()==KeyEvent.VK_ENTER){
             load_data_by_kelas();
         }else {
             JOptionPane.showMessageDialog(null, "Data Tidak ditemukan!");
         }
    }//GEN-LAST:event_cbKelasKeyPressed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:\
         load_data_by_kelas();
         width_column(); 
    }//GEN-LAST:event_jButton1ActionPerformed

    private void cbBulanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbBulanActionPerformed
        // TODO add your handling code here:
        tampil_bulan_terpilih();
    }//GEN-LAST:event_cbBulanActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Bukti_Pembayaran.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Bukti_Pembayaran.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Bukti_Pembayaran.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Bukti_Pembayaran.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Bukti_Pembayaran().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBayar;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnCetak;
    private javax.swing.JComboBox<String> cbBulan;
    private javax.swing.JComboBox<String> cbKelas;
    private javax.swing.JComboBox<String> cbTahun;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel tbiasa;
    private javax.swing.JTable tblSiswa;
    private javax.swing.JLabel txtIdBulan;
    private javax.swing.JLabel txtIdKelas;
    private javax.swing.JLabel txtIdSPP;
    private javax.swing.JLabel txtNamaSiswa;
    private javax.swing.JLabel txtNis;
    private javax.swing.JLabel txtNisn;
    private javax.swing.JLabel txtNominal;
    private javax.swing.JTextArea txtNota;
    private javax.swing.JLabel txtTanggalBayar;
    // End of variables declaration//GEN-END:variables
}
