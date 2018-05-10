package com.chaoyi.model

import com.google.gson.annotations.SerializedName

/**
 * Created by yupenglei on 2018/4/27.
 */

class PicData {

    /**
     * 20180427 : {"quotes":{"zh-Hans":"笑是健康的定义。 - 多丽丝·莱辛","en":"Laughter is by definition healthy. - Doris Lessing","zh-Hant":"笑是健康的定義。 - 多麗絲·萊辛"},"pic_url":"http://pics.tide.moreless.io/dailypics/FiNq8q8MR7GH7DI1CSZ8fxGx1WIu?imageMogr2/thumbnail/!2160x1920r|imageView2/1/w/2160/h/1920"}
     */

    @SerializedName("20180427")
    var data: Data? = null

    class Data {
        /**
         * quotes : {"zh-Hans":"笑是健康的定义。 - 多丽丝·莱辛","en":"Laughter is by definition healthy. - Doris Lessing","zh-Hant":"笑是健康的定義。 - 多麗絲·萊辛"}
         * pic_url : http://pics.tide.moreless.io/dailypics/FiNq8q8MR7GH7DI1CSZ8fxGx1WIu?imageMogr2/thumbnail/!2160x1920r|imageView2/1/w/2160/h/1920
         */

        var quotes: QuotesBean? = null
        var pic_url: String? = null

        class QuotesBean {
            /**
             * zh-Hans : 笑是健康的定义。 - 多丽丝·莱辛
             * en : Laughter is by definition healthy. - Doris Lessing
             * zh-Hant : 笑是健康的定義。 - 多麗絲·萊辛
             */

            @SerializedName("zh-Hans")
            var zhHans: String? = null
            var en: String? = null
            @SerializedName("zh-Hant")
            var zhHant: String? = null
        }
    }
}
