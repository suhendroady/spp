/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.spp;

import Config.koneksiDB;
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
public class Kelas extends javax.swing.JFrame {

    /**
     * Creates new form Setting_SPP
     */
    public Kelas() {
        initComponents();
        load_table();
        width_column();
        tampil_jurusan();
        btnEdit.setEnabled(false);
        btnCancel.setEnabled(false);
        txtIdKelas.setVisible(false);
    }
    
    private void load_table(){        
        // membuat tampilan model tabel
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("No.");
        model.addColumn("ID.");
        model.addColumn("Tahun Masuk");
        model.addColumn("Tingkat");
        model.addColumn("Kode Kelas");
        model.addColumn("Jurusan");
        model.addColumn("Kelas");
        
        //menampilkan data database kedalam tabel
        try {
            int no=1;
            
            //Buat variabel untuk mengambil tanggal hari ini
            Date currentDate = new Date();
            
            String sql = "SELECT * FROM kelas, jurusan WHERE kelas.id_jurusan=jurusan.id_jurusan ORDER BY kelas.tahun_angkatan DESC";
            java.sql.Connection conn=(Connection)koneksiDB.connectDB();
            java.sql.Statement stm=conn.createStatement();
            java.sql.ResultSet res=stm.executeQuery(sql);
            while(res.next()){
                String id = res.getString("id_kelas");
                String nama = res.getString("nama_kelas"); 
                String kode = res.getString("kode_kelas"); 
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
                    kelas = "X "+ nama;
                } else if (tingkat_int <= 24) {
                    kelas = "XI "+ nama;
                } else if(tingkat_int <= 36) {
                    kelas = "XII "+ nama;
                } else if(tingkat_int > 36) {
                    kelas = "Alumni "+ nama;
                }                
                
                //mengatur menjadi penyebutan dalam Rupiah
                /*Double y = Double.parseDouble(res.getString("nominal"));
                NumberFormat nf = NumberFormat.getCurrencyInstance();
                String nominal = nf.format(y);*/
                
                model.addRow(new Object[]{no++,id, tahun_terhitung, kelas, kode, jurusan, nama});
            }
            tblKelas.setModel(model);
        } catch (Exception e) {
        }
    }
    
    public void width_column(){        
        TableColumn column;
        tblKelas.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF); 
        //Nomor Urut
        column = tblKelas.getColumnModel().getColumn(0); 
        column.setPreferredWidth(40);
        //id Kelas
        column = tblKelas.getColumnModel().getColumn(1); 
        column.setPreferredWidth(40); 
        //Tahun Masuk
        column = tblKelas.getColumnModel().getColumn(2); 
        column.setPreferredWidth(80); 
        //Tingkat
        column = tblKelas.getColumnModel().getColumn(3); 
        column.setPreferredWidth(125);        
        //Kode Kelas
        column = tblKelas.getColumnModel().getColumn(4); 
        column.setPreferredWidth(100);      
        //Jurusan
        column = tblKelas.getColumnModel().getColumn(5); 
        column.setPreferredWidth(65);      
        //Kelas
        column = tblKelas.getColumnModel().getColumn(6); 
        column.setPreferredWidth(75);
    }
    
    public void kosong(){
        txtTahunMasuk.setText("");
        txtNamaKelas.setText("");
    }
    /*private void date(){
        //mengambil tanggal hari ini
        SimpleDateFormat dateFormat =new SimpleDateFormat("dd-MM-yyyy");
        java.util.Date date = new java.util.Date();
        txtTgl.setText(dateFormat.format(date));
    }*/
        
    public void tampil_jurusan(){
         try {
            int no=1;
            String sql = "SELECT id_jurusan, jurusan FROM jurusan ORDER BY id_jurusan ASC";
            java.sql.Connection conn=(Connection)koneksiDB.connectDB();
            java.sql.Statement stm=conn.createStatement();
            java.sql.ResultSet res=stm.executeQuery(sql);
            
            cbJurusan.addItem("-Pilih Jurusan-");
            while(res.next()){
                cbJurusan.addItem(res.getString("jurusan"));
            }
        }
        catch(Exception ex){
        }
    }
    
    public void tampil_id_jurusan(){
         try {
            int no=1;
            String sql = "SELECT id_jurusan, jurusan FROM jurusan WHERE jurusan= '" +cbJurusan.getSelectedItem()+ "'";
            java.sql.Connection conn=(Connection)koneksiDB.connectDB();
            java.sql.Statement stm=conn.createStatement();
            java.sql.ResultSet res=stm.executeQuery(sql);
            
            while(res.next()){
                txtIdJurusan.setText(res.getString("id_jurusan"));
            }
        }
        catch(Exception ex){
        }
    }
    
    public void simpan_kelas(){
        String tahun_masuk, nama_kelas, id_jurusan, kode_kelas, jurusan;
        tahun_masuk = txtTahunMasuk.getText();
        nama_kelas = txtNamaKelas.getText();
        id_jurusan = txtIdJurusan.getText();
        jurusan = (String) cbJurusan.getSelectedItem();
        kode_kelas = tahun_masuk+"-"+nama_kelas;
                
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
                    //INPUT KE Table PEMINJAMAN
                    String sql = "INSERT INTO kelas (nama_kelas, kode_kelas, id_jurusan, tahun_angkatan)"
                                                      + "VALUES ('" + nama_kelas + "','" + kode_kelas + "','" + id_jurusan + "',"
                                                      + "'" + tahun_masuk + "')";                                
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
    
    public void edit_kelas(){
        String id_kelas, tahun_masuk, nama_kelas, id_jurusan, kode_kelas, jurusan;
        id_kelas = txtIdKelas.getText();
        tahun_masuk = txtTahunMasuk.getText();
        nama_kelas = txtNamaKelas.getText();
        id_jurusan = txtIdJurusan.getText();
        jurusan = (String) cbJurusan.getSelectedItem();
        kode_kelas = tahun_masuk+"-"+nama_kelas;
                
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
                    //INPUT KE Table PEMINJAMAN
                    String sql = "UPDATE kelas SET nama_kelas = '" + nama_kelas + "', kode_kelas = '" + kode_kelas + "', tahun_angkatan = '" + tahun_masuk + "',"
                            + "id_jurusan = '" + id_jurusan + "' WHERE id_kelas = '" + id_kelas + "'";
                    java.sql.PreparedStatement pstb = conn.prepareStatement(sql);
                    pstb.execute();

                    JOptionPane.showMessageDialog(null, "Penyimpanan Data Berhasil");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, e.getMessage());
                }            
            load_table();            
            width_column();
            kosong();
            cbJurusan.setSelectedItem("-Pilih Jurusan-");
            btnSimpan.setEnabled(true);
            btnEdit.setEnabled(false);
            btnCancel.setEnabled(false);
        }
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
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtTahunMasuk = new javax.swing.JTextField();
        txtNamaKelas = new javax.swing.JTextField();
        cbJurusan = new javax.swing.JComboBox<>();
        btnSimpan = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblKelas = new javax.swing.JTable();
        txtIdJurusan = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        btnEdit = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        txtIdKelas = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 51));

        jLabel1.setFont(new java.awt.Font("Lato", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 51, 102));
        jLabel1.setText("Data Kelas");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel2.setFont(new java.awt.Font("Lato", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Tahun Masuk Siswa   :");

        jLabel3.setFont(new java.awt.Font("Lato", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Nama Kelas                   :");

        jLabel4.setFont(new java.awt.Font("Lato", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("Jurusan                           :");

        cbJurusan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbJurusanActionPerformed(evt);
            }
        });

        btnSimpan.setBackground(new java.awt.Color(51, 255, 51));
        btnSimpan.setFont(new java.awt.Font("Lato", 1, 14)); // NOI18N
        btnSimpan.setForeground(new java.awt.Color(255, 255, 255));
        btnSimpan.setText("Simpan");
        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(204, 255, 153));
        jPanel2.setForeground(new java.awt.Color(204, 255, 153));

        tblKelas.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tblKelas.setModel(new javax.swing.table.DefaultTableModel(
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
        tblKelas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblKelasMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblKelas);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        txtIdJurusan.setForeground(new java.awt.Color(51, 51, 51));

        jLabel5.setForeground(new java.awt.Color(255, 102, 51));
        jLabel5.setText("Ket: Nama Kelas Tanpa (Spasi)");

        btnEdit.setBackground(new java.awt.Color(51, 153, 255));
        btnEdit.setFont(new java.awt.Font("Lato", 1, 14)); // NOI18N
        btnEdit.setForeground(new java.awt.Color(255, 255, 255));
        btnEdit.setText("Edit");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
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

        txtIdKelas.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(18, 18, 18)
                                .addComponent(cbJurusan, 0, 163, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtNamaKelas))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtTahunMasuk))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGap(9, 9, 9)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtIdJurusan))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtIdKelas)
                            .addComponent(btnSimpan))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtTahunMasuk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtNamaKelas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(cbJurusan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtIdJurusan, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtIdKelas))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnEdit)
                            .addComponent(btnSimpan)
                            .addComponent(btnCancel))
                        .addGap(18, 18, 18))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        // TODO add your handling code here:
        simpan_kelas();
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void cbJurusanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbJurusanActionPerformed
        // TODO add your handling code here:
        tampil_id_jurusan();
    }//GEN-LAST:event_cbJurusanActionPerformed

    private void tblKelasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblKelasMouseClicked
        // TODO add your handling code here:
        int baris = tblKelas.rowAtPoint(evt.getPoint());
        String idKelas = tblKelas.getValueAt(baris, 1).toString();
        
        try {
        //int no=1;
            String sql = "SELECT * FROM kelas k, jurusan j WHERE "
                        + "k.id_jurusan = j.id_jurusan AND k.id_kelas = '" + idKelas + "'";
            java.sql.Connection conn=(Connection)koneksiDB.connectDB();
            java.sql.Statement stm=conn.createStatement();
            java.sql.ResultSet res=stm.executeQuery(sql);
            while(res.next()){
                String id_kelas = res.getString("id_kelas");
                String tahun_angkatan = res.getString("tahun_angkatan");                
                String nama_kelas = res.getString("nama_kelas");
                String jurusan = res.getString("jurusan");
                
                txtTahunMasuk.setText(tahun_angkatan);
                txtNamaKelas.setText(nama_kelas);
                cbJurusan.setSelectedItem(jurusan);
                txtIdKelas.setText(idKelas);
                btnSimpan.setEnabled(false);
                btnEdit.setEnabled(true);
                btnCancel.setEnabled(true);
                //cbSumberDana.setSelectedItem(sumber);
            }
        }catch (Exception e) {
        }
    }//GEN-LAST:event_tblKelasMouseClicked

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        edit_kelas();
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        btnSimpan.setEnabled(true);
        btnEdit.setEnabled(false);
        btnCancel.setEnabled(false);
        cbJurusan.setSelectedItem("-Pilih Jurusan-");
        kosong();
    }//GEN-LAST:event_btnCancelActionPerformed

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
            java.util.logging.Logger.getLogger(Kelas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Kelas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Kelas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Kelas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Kelas().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JComboBox<String> cbJurusan;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblKelas;
    private javax.swing.JLabel txtIdJurusan;
    private javax.swing.JLabel txtIdKelas;
    private javax.swing.JTextField txtNamaKelas;
    private javax.swing.JTextField txtTahunMasuk;
    // End of variables declaration//GEN-END:variables
}
