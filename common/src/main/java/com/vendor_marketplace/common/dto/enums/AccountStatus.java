package com.vendor_marketplace.common.dto.enums;

/**
  ACTIVE,               // Account is active and in good standing
  SUSPENDED,            // Account is temporarily suspended, possibly due to violation
  DEACTIVATED,          // Account is deactivated, user may have chosen to deactivate it
  BANNED,               // Account is permanently banned due to severe violation
  CLOSED                // Account is permanently closed, possibly at user request
 */

public enum AccountStatus {
    ACTIVE,
    SUSPENDED,
    DEACTIVATED,
    BANNED,
    CLOSED
}
