/*    */ package com.vcb.database.oracle;
/*    */ 
/*    */ import java.sql.CallableStatement;
/*    */ import java.sql.Connection;
/*    */ import java.sql.PreparedStatement;
/*    */ import java.sql.ResultSet;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DBUtils
/*    */ {
/*    */   public static void closeObject(ResultSet obj) {
/*    */     try {
/* 21 */       if (obj != null) {
/* 22 */         obj.close();
/*    */       }
/* 24 */     } catch (Throwable throwable) {}
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static void closeObject(CallableStatement obj) {
/*    */     try {
/* 31 */       if (obj != null) {
/* 32 */         obj.close();
/*    */       }
/* 34 */     } catch (Throwable throwable) {}
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static void closeObject(PreparedStatement obj) {
/*    */     try {
/* 41 */       if (obj != null) {
/* 42 */         obj.close();
/*    */       }
/* 44 */     } catch (Throwable throwable) {}
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static void closeObject(Connection obj) {
/*    */     try {
/* 51 */       if (obj != null) {
/* 52 */         obj.close();
/*    */       }
/* 54 */     } catch (Throwable throwable) {}
/*    */   }
/*    */ }


/* Location:              D:\Projects\test\ottJob\VCB_OTT_Jobs.jar!\com\vnpay\database\DBUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */