package com.tim.gulimall.order.web;

import com.tim.common.exception.NoStockException;
import com.tim.gulimall.order.service.OrderService;
import com.tim.gulimall.order.vo.OrderConfirmVo;
import com.tim.gulimall.order.vo.OrderSubmitVo;
import com.tim.gulimall.order.vo.SubmitOrderResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.concurrent.ExecutionException;

/**
 * @author tim
 * @date 2022/11/16 15:45
 **/
@Controller
public class OrderWebController {
    @Autowired
    OrderService orderService;


    /**
     * 去结算确认页
     *
     * @param model
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @GetMapping("/toTrade")
    public String toTrade(Model model) throws ExecutionException, InterruptedException {
        OrderConfirmVo confirmVo = orderService.confirmOrder();

        model.addAttribute("orderConfirmData", confirmVo);
        //展示订单确认的数据
        return "confirm";
    }

    /**
     * 提交订单
     *
     * @param vo
     * @return
     */
    @PostMapping("/submitOrder")
    public String submitOrder(OrderSubmitVo vo, Model model, RedirectAttributes redirectAttributes) {


        try {
            SubmitOrderResponseVo responseVo = orderService.submitOrder(vo);
            //下单失败回到订单确认页重新确认订单信息
            System.out.println("订单提交的数据..." + vo);
            if (responseVo.getCode() == 0) {
                //下单成功来到支付选择页
                model.addAttribute("submitOrderResp", responseVo);
                return "pay";
            } else {
                String msg = "下单失败；";
                switch (responseVo.getCode()) {
                    case 1:
                        msg += "订单信息过期，请刷新再次提交";
                        break;
                    case 2:
                        msg += "订单商品价格发生变化，请确认后再次提交";
                        break;
                    case 3:
                        msg += "库存锁定失败，商品库存不足";
                        break;
                    default:
                        msg += "未知错误，下单失败，请再次尝试";
                        break;
                }
                redirectAttributes.addFlashAttribute("msg", msg);
                return "redirect:http://order.gulimall.com/toTrade";
            }
        } catch (Exception e) {
            if (e instanceof NoStockException) {
                String message = ((NoStockException) e).getMessage();
                redirectAttributes.addFlashAttribute("msg", message);
            }

            return "redirect:http://order.gulimall.com/toTrade";
        }


    }
}
