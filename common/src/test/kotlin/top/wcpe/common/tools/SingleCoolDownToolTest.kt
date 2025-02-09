package top.wcpe.common.tools

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import top.wcpe.wcpelib.common.tools.SingleCoolDownTool

/**
 * 由 WCPE 在 2025/2/9 19:31 创建
 * <p>
 * Created by WCPE on 2025/2/9 19:31
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 */
class SingleCoolDownToolTest {

    @Test
    fun `test checkCoolDown when not in cooldown`() {
        val coolDownTool = SingleCoolDownTool()
        assertFalse(coolDownTool.checkCoolDown())
    }

    @Test
    fun `test checkCoolDown when in cooldown`() {
        val coolDownTool = SingleCoolDownTool()
        coolDownTool.setCoolDown(1000L)
        assertTrue(coolDownTool.checkCoolDown())
    }

    @Test
    fun `test setCoolDown`() {
        val coolDownTool = SingleCoolDownTool()
        coolDownTool.setCoolDown(1000L)
        assertTrue(coolDownTool.checkCoolDown())
    }

    @Test
    fun `test getRemainingCoolDown when not in cooldown`() {
        val coolDownTool = SingleCoolDownTool()
        assertEquals(true, coolDownTool.getRemainingCoolDown() < 0)
    }

    @Test
    fun `test getRemainingCoolDown when in cooldown`() {
        val coolDownTool = SingleCoolDownTool()
        coolDownTool.setCoolDown(1000L)
        val remaining = coolDownTool.getRemainingCoolDown()
        assertTrue(remaining > 0 && remaining <= 1000L)
    }

    @Test
    fun `test clearCoolDown`() {
        val coolDownTool = SingleCoolDownTool()
        coolDownTool.setCoolDown(1000L)
        coolDownTool.clearCoolDown()
        assertFalse(coolDownTool.checkCoolDown())
    }

    @Test
    fun `test coolDown with onSuccess callback`() {
        val coolDownTool = SingleCoolDownTool()
        var callbackCalled = false
        coolDownTool.coolDown(1000L) {
            callbackCalled = true
        }
        assertTrue(callbackCalled)
        assertTrue(coolDownTool.checkCoolDown())
    }

    @Test
    fun `test coolDown with onCooling callback`() {
        val coolDownTool = SingleCoolDownTool()
        var successCallbackCalled = false
        var coolingCallbackCalled = false
        var remainingCoolDown: Long = 0

        // First call to set cooldown
        coolDownTool.coolDown(1000L, {
            successCallbackCalled = true
        }, {
            coolingCallbackCalled = true
            remainingCoolDown = it
        })

        assertTrue(successCallbackCalled)
        assertFalse(coolingCallbackCalled)

        // Second call to trigger onCooling callback
        coolDownTool.coolDown(1000L, {
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
        val coolDownTool = SingleCoolDownTool()
        var callbackCalled = false
        coolDownTool.setCoolDown(1000L)
        Thread.sleep(1000L) // Wait for cooldown to finish
        coolDownTool.coolDown(1000L) {
            callbackCalled = true
        }
        assertTrue(callbackCalled)
    }
}