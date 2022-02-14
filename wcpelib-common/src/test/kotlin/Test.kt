
/**
 * 由 WCPE 在 2021/12/27 21:52 创建
 *
 * Created by WCPE on 2021/12/27 21:52
 *
 * Github: https://github.com/wcpe
 *
 * QQ: 1837019522
 *
 * @author WCPE
 */
class Test {
    /*
    fun testMojangApi() {
        println(sendGetMojangApi("paotangya"))
    }

    fun sendGetMojangApi(playerName: String): String? {
        var okHttpClient = OkHttpClient()


        val result = okHttpClient.newCall(
            Request.Builder()
                .url("https://api.mojang.com/users/profiles/minecraft/$playerName")
                .build()
        ).execute().body?.string()
        println(result)
        var jsonObject = JsonParser.parseString(result).asJsonObject
        val uid = jsonObject.getAsJsonPrimitive("id").asString

        val signature = okHttpClient.newCall(
            Request.Builder()
                .url("https://sessionserver.mojang.com/session/minecraft/profile/$uid")
                .build()
        ).execute().body?.string()
        println(signature)
        jsonObject = JsonParser.parseString(signature).asJsonObject
        jsonObject = jsonObject.getAsJsonArray("properties")[0].asJsonObject
        val value = jsonObject.getAsJsonPrimitive("value").asString
        val decoded = String(Base64.getDecoder().decode(value))
        println(decoded)
        jsonObject = JsonParser.parseString(decoded).asJsonObject
        val skinURL = jsonObject.getAsJsonObject("textures").getAsJsonObject("SKIN").getAsJsonPrimitive("url").asString
        val skinByte = "{\"textures\":{\"SKIN\":{\"url\":\"$skinURL\"}}}".toByteArray()
        return String(Base64.getEncoder().encode(skinByte))
    }
    */
}