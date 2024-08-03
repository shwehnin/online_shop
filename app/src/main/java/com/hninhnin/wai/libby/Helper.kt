package com.hninhnin.wai.libby

import android.util.Log

class Helper {
    companion object {
        var cartMap : HashMap<Int, Int> = hashMapOf()

        var USER_TOKEN : String? = null

        // debug log
        fun debugLog(msg: String) {
            Log.d("my_message", msg)
        }

        // check user token
        fun checkAuthUser() : Boolean{
            return USER_TOKEN == null
        }

        // add to cart
        fun addToCart(key: Int) {
            if(cartMap.containsKey(key)) {
                addQty(key)
            }else {
                cartMap[key] = 1
            }
        }

        // add quantity
        fun addQty(key:Int) {
            val oldQty = cartMap[key]!!
            val newQty = oldQty +1
            cartMap[key] = newQty
        }

        // remove from cart
        fun removeFromCart(key: Int) {
            if(cartMap.containsKey(key)) {
                cartMap.remove(key)
            }
        }

        // cart clear
        fun clearCart() {
            cartMap.clear()
        }

        // get all count from cart
        fun getCartCount() : Int {
            return cartMap.size
        }

        // get single item count
        fun getSingleItemCount(key: Int) : Int {
            if(cartMap.containsKey(key)) {
                return  cartMap[key]!!
            }
            return 0
        }

        // get all key
        fun getAllKeys() : String {
            var keys = ""
            for((k, v) in cartMap) {
                keys += "$k#$v," // according to server side
            }
            return keys
        }
    }
}