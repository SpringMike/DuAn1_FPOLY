/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raven.form;

import com.fpt.DAO.VoucherDAO;
import com.fpt.Validate.Validate;
import com.fpt.Validate.labelValidate;
import com.fpt.entity.Voucher;
import com.fpt.utils.MsgBox;
import com.fpt.utils.XDate;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ducit
 */
public class FormVoucher extends javax.swing.JPanel {

    /**
     * Creates new form FormVoucher
     */
    public FormVoucher() {
        initComponents();
        fillTable();
        setOpaque(false);
        btnDelete.setEnabled(false);
        btnUpdate.setEnabled(false);
    }

    VoucherDAO vDao = new VoucherDAO();

    public void fillTable() {
        DefaultTableModel model = (DefaultTableModel) tableShow.getModel();
        model.setRowCount(0);
        List<Voucher> list = vDao.selectAll();
        for (Voucher v : list) {
            model.addRow(new Object[]{
                v.getIdVoucher(), v.getNameVoucher(), v.getValue(), v.getQuatity(), v.getDateStart(), v.getDateEnd()
            });
        }
    }

    public void searchFillTable() {
        DefaultTableModel model = (DefaultTableModel) tableShow.getModel();
        model.setRowCount(0);
        String keyString = txtSearch.getText();
        List<Voucher> list = vDao.selectByKeyWord(keyString);
        if (list.isEmpty()) {
            lblSearch.setText("Không có Voucher " + keyString);
            return;
        }
        for (Voucher v : list) {
            model.addRow(new Object[]{
                v.getIdVoucher(), v.getNameVoucher(), v.getValue(), v.getQuatity(), v.getDateStart(), v.getDateEnd()
            });
        }
        lblSearch.setText("");
    }

    public void setForm(Voucher c) {
        txtCodeVoucher.setText(c.getNameVoucher());
        txtDateEnd.setText(XDate.toString(c.getDateEnd(), "dd-MM-yyyy"));
        txtDateStart.setText(XDate.toString(c.getDateStart(), "dd-MM-yyyy"));
        txtQuatity.setText(String.valueOf(c.getQuatity()));
        txtValue.setText(String.valueOf(c.getValue()));
    }

    Voucher getForm() {
        Voucher v = new Voucher();
        v.setDateEnd(XDate.toDate(txtDateEnd.getText(), "dd-MM-yyyy"));
        v.setDateStart(XDate.toDate(txtDateStart.getText(), "dd-MM-yyyy"));
        v.setNameVoucher(txtCodeVoucher.getText());
        v.setQuatity(Integer.parseInt(txtQuatity.getText()));
        v.setValue(Integer.parseInt(txtValue.getText()));
        return v;
    }

    public void edit() {
        int row = tableShow.getSelectedRow();
        txtCodeVoucher.setEditable(false);
        btnAdd.setEnabled(false);
        btnDelete.setEnabled(true);
        btnUpdate.setEnabled(true);
        int code = (int) tableShow.getValueAt(row, 0);
        Voucher v = vDao.selectById(code);
        setForm(v);
    }

    public void clearForm() {
        txtCodeVoucher.setEditable(true);
        txtCodeVoucher.setText("");
        txtDateEnd.setText("");
        txtDateStart.setText("");
        txtValue.setText("");
        txtQuatity.setText("");
        btnAdd.setEnabled(true);
        btnDelete.setEnabled(true);
        btnNew.setEnabled(true);
        btnUpdate.setEnabled(true);
        lblCodeVoucher.setText("");
        lblDateEnd.setText("");
        lblDateStart.setText("");
        lblQuatity.setText("");
        lblValue.setText("");
        btnDelete.setEnabled(false);
        btnUpdate.setEnabled(false);

    }

    public boolean checkVoucher(String acc) {
        for (int i = 0; i < vDao.selectAll().size(); i++) {
            if (vDao.selectAll().get(i).getNameVoucher().trim().equalsIgnoreCase(acc.trim())) {
                return true;
            }
        }
        return false;
    }

    public void insert() {
        try {
            if (!Validate.checkEmpty(lblCodeVoucher, txtCodeVoucher, "Không bỏ trống mã")) {
                return;
            } else if (!Validate.checkLength(lblCodeVoucher, txtCodeVoucher, "Mã Voucher tối đa 7 ký tự", 7)) {
                return;
            } else if (checkVoucher(txtCodeVoucher.getText()) == true) {
                MsgBox.labelAlert(lblCodeVoucher, txtCodeVoucher, "Mã voucher đã tồn tại");
                return;
            } else if (!Validate.checkEmpty(lblValue, txtValue, "Không bỏ trống %")) {
                return;
            } else if (!Validate.checkNumber(lblValue, txtValue, "Giá trị không hợp lệ")) {
                return;
            } else if (!Validate.checkEmpty(lblQuatity, txtQuatity, "Không bỏ trống số lượng")) {
                return;
            } else if (!Validate.checkNumber(lblQuatity, txtQuatity, "Giá trị không hợp lệ")) {
                return;
            } else if (!Validate.checkEmpty(lblDateStart, txtDateStart, "Không bỏ trống ngày bắt đầu")) {
                return;
            } else if (!Validate.checkEmpty(lblDateEnd, txtDateEnd, "Không bỏ trống ngày kết thúc")) {
                return;
            } else {
                Voucher v = getForm();
                vDao.insert(v);
                clearForm();
                fillTable();
                MsgBox.alert(this, "Thêm mới thành công");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateVoucher() {
        try {
            if (!Validate.checkEmpty(lblValue, txtValue, "Không bỏ trống %")) {
                return;
            } else if (!Validate.checkNumber(lblValue, txtValue, "Giá trị không hợp lệ")) {
                return;
            } else if (!Validate.checkEmpty(lblQuatity, txtQuatity, "Không bỏ trống số lượng")) {
                return;
            } else if (!Validate.checkNumber(lblQuatity, txtQuatity, "Giá trị không hợp lệ")) {
                return;
            } else if (!Validate.checkEmpty(lblDateStart, txtDateStart, "Không bỏ trống ngày bắt đầu")) {
                return;
            } else if (!Validate.checkEmpty(lblDateEnd, txtDateEnd, "Không bỏ trống ngày kết thúc")) {
                return;
            } else {
                Voucher v = getForm();
                int row = tableShow.getSelectedRow();
                v.setIdVoucher((int) tableShow.getValueAt(row, 0));
                vDao.update(v);
                clearForm();
                fillTable();
                MsgBox.alert(this, "Update thành công");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete() {
        int row = tableShow.getSelectedRow();
        int code = (int) tableShow.getValueAt(row, 0);
        if (MsgBox.confirm(this, "Bạn có muốn xóa không?")) {
            try {
                vDao.delete(code);
                fillTable();
                clearForm();
                MsgBox.alert(this, "Xoá Thành công");
            } catch (Exception e) {
                e.printStackTrace();
            }
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

        dateStart = new com.raven.datechooser.DateChooser();
        dateEnd = new com.raven.datechooser.DateChooser();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableShow = new com.raven.suportSwing.TableColumn();
        jLabel1 = new javax.swing.JLabel();
        txtValue = new com.raven.suportSwing.TextField();
        txtCodeVoucher = new com.raven.suportSwing.TextField();
        txtDateStart = new com.raven.suportSwing.TextField();
        txtDateEnd = new com.raven.suportSwing.TextField();
        txtQuatity = new com.raven.suportSwing.TextField();
        btnAdd = new com.raven.suportSwing.MyButton();
        btnUpdate = new com.raven.suportSwing.MyButton();
        btnDelete = new com.raven.suportSwing.MyButton();
        btnNew = new com.raven.suportSwing.MyButton();
        lblCodeVoucher = new javax.swing.JLabel();
        lblValue = new javax.swing.JLabel();
        lblQuatity = new javax.swing.JLabel();
        lblDateStart = new javax.swing.JLabel();
        lblDateEnd = new javax.swing.JLabel();
        txtSearch = new com.raven.suportSwing.TextField();
        btnSearch = new com.raven.suportSwing.MyButton();
        lblSearch = new javax.swing.JLabel();

        dateStart.setTextRefernce(txtDateStart);

        dateEnd.setTextRefernce(txtDateEnd);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        tableShow.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID Voucher", "Mã Voucher", "Phần % giảm", "Số lượng", "Ngày bắt đầu", "Ngày kết thúc"
            }
        ));
        tableShow.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableShowMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tableShow);

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel1.setText("Khuyến Mãi");

        txtValue.setLabelText("Giảm % ?");
        txtValue.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtValueFocusGained(evt);
            }
        });

        txtCodeVoucher.setLabelText("Mã Voucher");
        txtCodeVoucher.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtCodeVoucherFocusGained(evt);
            }
        });

        txtDateStart.setLabelText("Ngày bắt đầu");
        txtDateStart.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtDateStartFocusGained(evt);
            }
        });

        txtDateEnd.setLabelText("Ngày kết thúc");
        txtDateEnd.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtDateEndFocusGained(evt);
            }
        });

        txtQuatity.setLabelText("Số lượng");
        txtQuatity.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtQuatityFocusGained(evt);
            }
        });

        btnAdd.setText("Thêm");
        btnAdd.setRadius(20);
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnUpdate.setText("Sửa");
        btnUpdate.setRadius(20);
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        btnDelete.setText("Xoá");
        btnDelete.setRadius(20);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnNew.setText("Tạo mới");
        btnNew.setRadius(20);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });

        lblCodeVoucher.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        lblCodeVoucher.setForeground(new java.awt.Color(255, 0, 0));

        lblValue.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        lblValue.setForeground(new java.awt.Color(255, 0, 0));

        lblQuatity.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        lblQuatity.setForeground(new java.awt.Color(255, 0, 0));

        lblDateStart.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        lblDateStart.setForeground(new java.awt.Color(255, 0, 0));

        lblDateEnd.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        lblDateEnd.setForeground(new java.awt.Color(255, 0, 0));

        txtSearch.setLabelText("Tìm theo mã");
        txtSearch.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtSearchFocusGained(evt);
            }
        });
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSearchKeyPressed(evt);
            }
        });

        btnSearch.setText("Tìm");
        btnSearch.setRadius(20);
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        lblSearch.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        lblSearch.setForeground(new java.awt.Color(255, 0, 0));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtDateEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(txtDateStart, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txtQuatity, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(txtValue, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(txtCodeVoucher, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 635, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                                        .addGap(22, 22, 22)
                                                        .addComponent(lblCodeVoucher, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                                        .addGap(22, 22, 22)
                                                        .addComponent(lblValue, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                                        .addGap(22, 22, 22)
                                                        .addComponent(lblQuatity, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                                        .addGap(22, 22, 22)
                                                        .addComponent(lblDateStart, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                                        .addGap(18, 18, 18)
                                                        .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                                        .addGap(22, 22, 22)
                                                        .addComponent(lblDateEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE))))))))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(69, 69, 69)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblSearch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtSearch, javax.swing.GroupLayout.DEFAULT_SIZE, 383, Short.MAX_VALUE))
                        .addGap(50, 50, 50)
                        .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(53, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addComponent(lblSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txtCodeVoucher, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblCodeVoucher, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(lblValue, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtQuatity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblQuatity, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDateStart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(3, 3, 3)
                        .addComponent(lblDateStart, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDateEnd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(lblDateEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 426, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // TODO add your handling code here:
        insert();
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        delete();
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:'
        clearForm();
    }//GEN-LAST:event_btnNewActionPerformed

    private void tableShowMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableShowMouseClicked
        // TODO add your handling code here:
        edit();
    }//GEN-LAST:event_tableShowMouseClicked

    private void txtCodeVoucherFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCodeVoucherFocusGained
        // TODO add your handling code here:
        lblCodeVoucher.setText("");
    }//GEN-LAST:event_txtCodeVoucherFocusGained

    private void txtValueFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtValueFocusGained
        // TODO add your handling code here:
        lblValue.setText("");

    }//GEN-LAST:event_txtValueFocusGained

    private void txtQuatityFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtQuatityFocusGained
        // TODO add your handling code here:
        lblQuatity.setText("");
    }//GEN-LAST:event_txtQuatityFocusGained

    private void txtDateStartFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDateStartFocusGained
        // TODO add your handling code here:
        lblDateStart.setText("");
    }//GEN-LAST:event_txtDateStartFocusGained

    private void txtDateEndFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDateEndFocusGained
        // TODO add your handling code here:
        lblDateEnd.setText("");
    }//GEN-LAST:event_txtDateEndFocusGained

    private void txtSearchFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSearchFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSearchFocusGained

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        // TODO add your handling code here:
        searchFillTable();
    }//GEN-LAST:event_btnSearchActionPerformed

    private void txtSearchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyPressed
        // TODO add your handling code here:
        searchFillTable();
    }//GEN-LAST:event_txtSearchKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.raven.suportSwing.MyButton btnAdd;
    private com.raven.suportSwing.MyButton btnDelete;
    private com.raven.suportSwing.MyButton btnNew;
    private com.raven.suportSwing.MyButton btnSearch;
    private com.raven.suportSwing.MyButton btnUpdate;
    private com.raven.datechooser.DateChooser dateEnd;
    private com.raven.datechooser.DateChooser dateStart;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblCodeVoucher;
    private javax.swing.JLabel lblDateEnd;
    private javax.swing.JLabel lblDateStart;
    private javax.swing.JLabel lblQuatity;
    private javax.swing.JLabel lblSearch;
    private javax.swing.JLabel lblValue;
    private com.raven.suportSwing.TableColumn tableShow;
    private com.raven.suportSwing.TextField txtCodeVoucher;
    private com.raven.suportSwing.TextField txtDateEnd;
    private com.raven.suportSwing.TextField txtDateStart;
    private com.raven.suportSwing.TextField txtQuatity;
    private com.raven.suportSwing.TextField txtSearch;
    private com.raven.suportSwing.TextField txtValue;
    // End of variables declaration//GEN-END:variables
}
