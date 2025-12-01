package com.vendor_marketplace.common.helper;

public class EmailSendingTemplate {

    public static String sendEmailForOTP(String name, String OTP) {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            line-height: 1.6;
                            color: #333;
                            max-width: 600px;
                            margin: 0 auto;
                            padding: 20px;
                        }
                        .header {
                            background-color: #f2f2f3;
                            color: rgb(10, 115, 165);
                            padding: 10px 20px;
                            text-align: center;
                            border-radius: 5px 5px 0 0;
                        }
                        .content {
                            padding: 20px;
                            border: 1px solid #ddd;
                            border-top: none;
                            border-radius: 0 0 5px 5px;
                        }
                        .otp {
                            font-size: 24px;
                            font-weight: bold;
                            color: rgb(10, 115, 165);
                            text-align: center;
                            margin: 20px 0;
                        }
                        .footer {
                            margin-top: 20px;
                            font-size: 0.8em;
                            color: #777;
                            text-align: center;
                        }
                    </style>
                </head>
                <body>
                    <div class="header">
                        <h2>Email Verify OTP</h2>
                    </div>
                    <div class="content">
                        <p>Hello %s,</p>
                        <p>Thanks for choosing us. Please use the following OTP to proceed:</p>
                        <div class="otp">%s</div>
                        <p>Use this OTP when you Login</p>
                        <p>For security reasons, don't share this OTP with anyone.</p>
                    </div>
                    <div class="footer">
                        <p>© 2025 Vendor Marketplace. All rights reserved.</p>
                    </div>
                </body>
                </html>
                """.formatted(name, OTP);
    }

    public static String sendEmailForOTPWithFrontendUrl(String name, String OTP, String frontendUrl) {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            line-height: 1.6;
                            color: #333;
                            max-width: 600px;
                            margin: 0 auto;
                            padding: 20px;
                        }
                        .header {
                            background-color: #f2f2f3;
                            color: rgb(10, 115, 165);
                            padding: 10px 20px;
                            text-align: center;
                            border-radius: 5px 5px 0 0;
                        }
                        .content {
                            padding: 20px;
                            border: 1px solid #ddd;
                            border-top: none;
                            border-radius: 0 0 5px 5px;
                        }
                        .otp {
                            font-size: 24px;
                            font-weight: bold;
                            color: rgb(10, 115, 165);
                            text-align: center;
                            margin: 20px 0;
                        }
                        .footer {
                            margin-top: 20px;
                            font-size: 0.8em;
                            color: #777;
                            text-align: center;
                        }
                    </style>
                </head>
                <body>
                    <div class="header">
                        <h2>Email Verification OTP</h2>
                    </div>
                    <div class="content">
                        <p>Hello %s,</p>
                        <p>Thanks for choosing us. Please use the following OTP to proceed:</p>
                        <div class="otp">%s</div>
                        <p>Use this OTP when you login.</p>
                        <a href="%s" style="display:inline-block;padding:10px 20px;
                            background-color:#4CAF50;color:#fff;text-decoration:none;
                            border-radius:5px;margin:15px 0;">
                            Click here to verify email
                        </a>
                        <p>For security reasons, do not share this OTP with anyone.</p>
                    </div>
                    <div class="footer">
                        <p>© 2025 Vendor Marketplace. All rights reserved.</p>
                    </div>
                </body>
                </html>
                """.formatted(name, OTP, frontendUrl);
    }

    public static String sendEmailForOrderStatus(String name, String orderId, String orderStatus, String email,String paymentStatus) {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            line-height: 1.6;
                            color: #333;
                            max-width: 600px;
                            margin: 0 auto;
                            padding: 20px;
                        }
                        .header {
                            background-color: #f2f2f3;
                            color: rgb(10, 115, 165);
                            padding: 10px 20px;
                            text-align: center;
                            border-radius: 5px 5px 0 0;
                        }
                        .content {
                            padding: 20px;
                            border: 1px solid #ddd;
                            border-top: none;
                            border-radius: 0 0 5px 5px;
                        }
                        .order-info {
                            background-color: #f9f9f9;
                            padding: 20px;
                            border-radius: 5px;
                            margin: 20px 0;
                            border-left: 4px solid #0c73a5;
                        }
                        .status {
                            font-size: 20px;
                            font-weight: bold;
                            text-align: center;
                            margin: 20px 0;
                            padding: 12px;
                            border-radius: 5px;
                        }
                        .status-pending {
                            background-color: #fff3cd;
                            color: #856404;
                            border: 2px solid #ffc107;
                        }
                        .status-completed {
                            background-color: #d4edda;
                            color: #155724;
                            border: 2px solid #28a745;
                        }
                        .status-cancelled {
                            background-color: #f8d7da;
                            color: #721c24;
                            border: 2px solid #dc3545;
                        }
                        .info-row {
                            margin: 12px 0;
                            padding: 8px 0;
                            border-bottom: 1px solid #eee;
                        }
                        .info-label {
                            font-weight: bold;
                            color: #555;
                            display: inline-block;
                            width: 100px;
                        }
                        .footer {
                            margin-top: 20px;
                            font-size: 0.8em;
                            color: #777;
                            text-align: center;
                        }
                    </style>
                </head>
                <body>
                    <div class="header">
                        <h2>Order Status Information</h2>
                    </div>
                    <div class="content">
                        <p>Hello %s,</p>
                        <p>Here is your order status information:</p>
                
                        <div class="order-info">
                            <div class="info-row">
                                <span class="info-label">Order ID:</span> %s
                            </div>
                            <div class="info-row">
                                <span class="info-label">Email:</span> %s
                            </div>
                            <div class="info-row">
                                <span class="info-label">Order Status: %s</span>
                            </div>
                             <div class="info-row">
                                <span class="info-label">Payment Status: %s</span>
                            </div>
                        </div>
                        <p>Thank you for your order! We appreciate your business.</p>
                    </div>
                    <div class="footer">
                        <p>© 2025 Vendor Marketplace. All rights reserved.</p>
                    </div>
                </body>
                </html>
                """.formatted(name, orderId, email, orderStatus,paymentStatus);
    }


    public static String sendWelcomeEmail(String name) {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            line-height: 1.6;
                            color: #333;
                            max-width: 600px;
                            margin: 0 auto;
                            padding: 20px;
                        }
                        .header {
                            background-color: rgb(10, 115, 165);
                            color: white;
                            padding: 10px 20px;
                            text-align: center;
                            border-radius: 5px 5px 0 0;
                        }
                        .content {
                            padding: 20px;
                            border: 1px solid #ddd;
                            border-top: none;
                            border-radius: 0 0 5px 5px;
                        }
                        .footer {
                            margin-top: 20px;
                            font-size: 0.8em;
                            color: #777;
                            text-align: center;
                        }
                    </style>
                </head>
                <body>
                    <div class="header">
                        <h2>Welcome to Vendor Marketplace!</h2>
                    </div>
                    <div class="content">
                        <p>Hello %s,</p>
                        <p>We’re thrilled to have you on board 🎉</p>
                        <p>Thank you for choosing <strong>Your Own Vendor</strong>. We’re committed to bringing you the best online shopping experience with quality products and trusted sellers.</p>
                        <p>Start exploring amazing deals today!</p>
                    </div>
                    <div class="footer">
                        <p>© 2025 Vendor Marketplace. All rights reserved.</p>
                    </div>
                </body>
                </html>
                """.formatted(name);
    }


}
