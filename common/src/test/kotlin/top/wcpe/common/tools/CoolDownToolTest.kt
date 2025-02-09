package top.wcpe.common.tools

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import top.wcpe.wcpelib.common.tools.CoolDownTool

/**
 * 由 WCPE 在 2025/2/9 20:00 创建
 * <p>
 * Created by WCPE on 2025/2/9 20:00
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 */
class CoolDownToolTest {

    @Test
    fun `test checkCoolDown when not in cooldown`() {
        val coolDownTool = CoolDownTool()
        assertFalse(coolDownTool.checkCoolDown("key1"))
    }

    @Test
    fun `test checkCoolDown when in cooldown`() {
        val coolDownTool = CoolDownTool()
        coolDownTool.setCoolDown("key1", 1000L)
        assertTrue(coolDownTool.checkCoolDown("key1"))
    }

    @Test
    fun `test checkCoolDown after cooldown expires`() {
        val coolDownTool = CoolDownTool()
        coolDownTool.setCoolDown("key1", 1000L)
        Thread.sleep(1000L) // Wait for cooldown to finish
        assertFalse(coolDownTool.checkCoolDown("key1"))
    }

    @Test
    fun `test setCoolDown`() {
        val coolDownTool = CoolDownTool()
        coolDownTool.setCoolDown("key1", 1000L)
        assertTrue(coolDownTool.checkCoolDown("key1"))
    }

    @Test
    fun `test getRemainingCoolDown when not in cooldown`() {
        val coolDownTool = CoolDownTool()
        assertEquals(-1L, coolDownTool.getRemainingCoolDown("key1"))
    }

    @Test
    fun `test getRemainingCoolDown when in cooldown`() {
        val coolDownTool = CoolDownTool()
        coolDownTool.setCoolDown("key1", 1000L)
        val remaining = coolDownTool.getRemainingCoolDown("key1")
        assertTrue(remaining > 0 && remaining <= 1000L)
    }

    @Test
    fun `test clearCoolDown`() {
        val coolDownTool = CoolDownTool()
        coolDownTool.setCoolDown("key1", 1000L)
        coolDownTool.clearCoolDown("key1")
        assertFalse(coolDownTool.checkCoolDown("key1"))
    }

    @Test
    fun `test clearAllCoolDown`() {
        val coolDownTool = CoolDownTool()
        coolDownTool.setCoolDown("key1", 1000L)
        coolDownTool.setCoolDown("key2", 1000L)
        coolDownTool.clearAllCoolDown()
        assertFalse(coolDownTool.checkCoolDown("key1"))
        assertFalse(coolDownTool.checkCoolDown("key2"))
    }

    @Test
    fun `test getAllCoolDown`() {
        val coolDownTool = CoolDownTool()
        coolDownTool.setCoolDown("key1", 1000L)
        coolDownTool.setCoolDown("key2", 2000L)
        val allCoolDown = coolDownTool.getAllCoolDown()
        assertEquals(2, allCoolDown.size)
        assertTrue(allCoolDown.containsKey("key1"))
        assertTrue(allCoolDown.containsKey("key2"))
    }

    @Test
    fun `test coolDown with onSuccess callback`() {
        val coolDownTool = CoolDownTool()
        var callbackCalled = false
        coolDownTool.coolDown("key1", 1000L) {
            callbackCalled = true
        }
        assertTrue(callbackCalled)
        assertTrue(coolDownTool.checkCoolDown("key1"))
    }

    @Test
    fun `test coolDown with onCooling callback`() {
        val coolDownTool = CoolDownTool()
        var successCallbackCalled = false
        var coolingCallbackCalled = false
        var remainingCoolDown: Long = 0

        // First call to set cooldown
        coolDownTool.coolDown("key1", 1000L, {
            successCallbackCalled = true
        }, {
            coolingCallbackCalled = true
            remainingCoolDown = it
        })

        assertTrue(successCallbackCalled)
        assertFalse(coolingCallbackCalled)

        // Second call to trigger onCooling callback
        coolDownTool.coolDown("key1", 1000L, {
            successCallbackCalled = true
        }, {
            coolingCallbackCalled = true
            remainingCoolDown = it
        })

        assertTrue(coolingCallbackCalled)
        assertTrue(remainingCoolDown > 0 && remainingCoolDown <= 1000L)
    }

    @Test
    fun `test coolDown after cooldown period`() {
        val coolDownTool = CoolDownTool()
        var callbackCalled = false
        coolDownTool.setCoolDown("key1", 1000L)
        Thread.sleep(1000L) // Wait for cooldown to finish
        coolDownTool.coolDown("key1", 1000L) {
            callbackCalled = true
        }
        assertTrue(callbackCalled)
    }
}