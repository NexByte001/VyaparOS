/**
 * VyaparOS - Unified Vanilla JS API Client & Workspace Manager
 * Modern SaaS Business Operating System
 */

function getAppBasePath() {
  const path = window.location.pathname;
  if (path.includes('/frontend/')) {
    return path.substring(0, path.indexOf('/frontend/') + '/frontend/'.length - 1);
  }
  return '';
}

function resolveApiBaseUrl() {
  // 1. Explicit window-level override
  if (typeof window.API_BASE_URL === 'string' && window.API_BASE_URL.trim() !== '') {
    return window.API_BASE_URL.trim();
  }
  // 2. Local storage override (configurable by user/admin in browser)
  const storedUrl = localStorage.getItem('vyparos_api_url');
  if (storedUrl && storedUrl.trim() !== '') {
    return storedUrl.trim();
  }
  // 3. Localhost development default
  const isLocal = ['localhost', '127.0.0.1', ''].includes(window.location.hostname);
  if (isLocal) {
    return 'http://localhost:8080/api';
  }
  // 4. Production hosting:
  // If hosted behind a reverse proxy (e.g. Nginx, Docker, Caddy), use relative /api
  // Otherwise if on static hosts (Vercel, Netlify), allow stored URL or backend fallback
  if (window.location.hostname.endsWith('vercel.app') || window.location.hostname.endsWith('netlify.app')) {
    return localStorage.getItem('vyparos_api_url') || 'https://vyaparos-production.up.railway.app/api';
    
  }
  return `${window.location.origin}/api`;
}

const API_CONFIG = {
  get baseUrl() {
    return resolveApiBaseUrl();
  },
  set baseUrl(val) {
    if (val) {
      localStorage.setItem('vyparos_api_url', val);
    } else {
      localStorage.removeItem('vyparos_api_url');
    }
  }
};

const Storage = {
  getToken() {
    return sessionStorage.getItem('vyparos_jwt');
  },
  setToken(token) {
    sessionStorage.setItem('vyparos_jwt', token);
  },
  getUser() {
    const raw = sessionStorage.getItem('vyparos_user');
    return raw ? JSON.parse(raw) : null;
  },
  setUser(user) {
    sessionStorage.setItem('vyparos_user', JSON.stringify(user));
  },
  getActiveBusiness() {
    const raw = sessionStorage.getItem('vyparos_active_biz');
    return raw ? JSON.parse(raw) : null;
  },
  setActiveBusiness(biz) {
    sessionStorage.setItem('vyparos_active_biz', JSON.stringify(biz));
  },
  clear() {
    sessionStorage.removeItem('vyparos_jwt');
    sessionStorage.removeItem('vyparos_user');
    sessionStorage.removeItem('vyparos_active_biz');
  }
};

async function apiRequest(endpoint, options = {}) {
  const currentBase = API_CONFIG.baseUrl.replace(/\/+$/, '');
  const cleanEndpoint = endpoint.startsWith('/') ? endpoint : '/' + endpoint;
  const url = `${currentBase}${cleanEndpoint}`;
  const token = Storage.getToken();

  const headers = {
    'Content-Type': 'application/json',
    'Accept': 'application/json',
    ...(options.headers || {})
  };

  if (token) {
    headers['Authorization'] = `Bearer ${token}`;
  }

  try {
    const response = await fetch(url, {
      ...options,
      headers
    });

    const data = await response.json().catch(() => null);

    if (!response.ok) {
      if (response.status === 401 && !endpoint.includes('/auth/login') && !endpoint.includes('/auth/register')) {
        Storage.clear();
        showToast('Session expired. Please log in again.', 'error');
        setTimeout(() => {
          window.location.href = `${getAppBasePath()}/login.html`;
        }, 1200);
      }
      
      const errorMsg = (data && data.message) || `HTTP Error ${response.status}: ${response.statusText || 'Request failed'}`;
      throw new Error(errorMsg);
    }

    return data;
  } catch (err) {
    console.error(`[VyaparOS API Error] on ${url}:`, err);
    if (err.message && err.message.toLowerCase().includes('failed to fetch')) {
      const isLocal = ['localhost', '127.0.0.1'].includes(window.location.hostname);
      const friendlyMsg = isLocal
        ? `Unable to connect to backend at ${currentBase}. Ensure Spring Boot is running on port 8080.`
        : `Unable to reach backend at ${currentBase}. Please verify your backend deployment URL and CORS settings.`;
      showToast(friendlyMsg, 'error');
      throw new Error(friendlyMsg);
    }
    throw err;
  }
}

function showToast(message, type = 'success') {
  let container = document.getElementById('toast-container');
  if (!container) {
    container = document.createElement('div');
    container.id = 'toast-container';
    document.body.appendChild(container);
  }

  const toast = document.createElement('div');
  toast.className = `toast toast-${type}`;
  toast.innerHTML = `
    <span>${message}</span>
    <button style="background:none;border:none;color:#fff;margin-left:12px;cursor:pointer;font-size:1rem;" onclick="this.parentElement.remove()">✕</button>
  `;

  container.appendChild(toast);

  setTimeout(() => {
    toast.style.opacity = '0';
    setTimeout(() => toast.remove(), 300);
  }, 4500);
}

function checkAuthGuard() {
  const token = Storage.getToken();
  if (!token) {
    window.location.href = `${getAppBasePath()}/login.html`;
    return false;
  }
  return true;
}

const CURRENCY_LOCALE_MAP = {
  'INR': 'en-IN',
  'USD': 'en-US',
  'EUR': 'en-IE',
  'GBP': 'en-GB',
  'AED': 'en-AE',
  'CAD': 'en-CA',
  'AUD': 'en-AU',
  'SGD': 'en-SG',
  'JPY': 'ja-JP'
};

function isValidCurrencyCode(code) {
  if (typeof code !== 'string') return false;
  const clean = code.trim().toUpperCase();
  if (!/^[A-Z]{3}$/.test(clean)) return false;
  try {
    new Intl.NumberFormat('en-US', { style: 'currency', currency: clean });
    return true;
  } catch {
    return false;
  }
}

function formatCurrency(amount, currency) {
  let targetAmount = amount;
  let targetCurrency = currency;

  // 1. Detect swapped arguments: formatCurrency('INR', 1000000)
  if (typeof targetAmount === 'string' && isValidCurrencyCode(targetAmount) && (typeof targetCurrency === 'number' || (!isNaN(Number(targetCurrency)) && targetCurrency !== null && targetCurrency !== ''))) {
    const temp = targetAmount;
    targetAmount = targetCurrency;
    targetCurrency = temp;
  }

  // 2. Parse numeric amount safely
  let num = 0;
  if (targetAmount !== undefined && targetAmount !== null && targetAmount !== '') {
    num = typeof targetAmount === 'number' ? targetAmount : parseFloat(targetAmount);
    if (isNaN(num)) num = 0;
  }

  // 3. Resolve and sanitize currency code
  // If targetCurrency is a number or numeric string (e.g. 1000000), it is an amount, not a currency code
  let resolvedCode = 'INR';
  const activeBiz = Storage.getActiveBusiness();
  const bizCurrency = activeBiz && activeBiz.currency;

  if (typeof targetCurrency === 'string' && isValidCurrencyCode(targetCurrency)) {
    resolvedCode = targetCurrency.trim().toUpperCase();
  } else if (typeof bizCurrency === 'string' && isValidCurrencyCode(bizCurrency)) {
    resolvedCode = bizCurrency.trim().toUpperCase();
  }

  // 4. Determine locale (en-IN for INR supports Indian lakhs/crores formatting)
  const locale = CURRENCY_LOCALE_MAP[resolvedCode] || 'en-IN';

  // 5. Format with fallback safety to prevent any UI crash
  try {
    return new Intl.NumberFormat(locale, {
      style: 'currency',
      currency: resolvedCode,
      minimumFractionDigits: 2,
      maximumFractionDigits: 2
    }).format(num);
  } catch (err) {
    console.warn(`[VyaparOS Currency Warning] Failed formatting with currency '${resolvedCode}':`, err);
    try {
      return new Intl.NumberFormat('en-IN', {
        style: 'currency',
        currency: 'INR',
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      }).format(num);
    } catch {
      return `₹${num.toFixed(2)}`;
    }
  }
}

function formatDate(dateStr) {
  if (!dateStr) return '-';
  const d = new Date(dateStr);
  return d.toLocaleDateString('en-IN', { day: 'numeric', month: 'short', year: 'numeric' });
}

function formatDateTime(dateStr) {
  if (!dateStr) return '-';
  const d = new Date(dateStr);
  return d.toLocaleString('en-IN', {
    day: 'numeric', month: 'short', year: 'numeric',
    hour: '2-digit', minute: '2-digit'
  });
}

function renderSidebar(activeNavKey = 'dashboard') {
  const sidebarContainer = document.getElementById('sidebar-container');
  if (!sidebarContainer) return;

  const base = getAppBasePath();
  const activeBiz = Storage.getActiveBusiness() || { businessName: 'VyaparOS Workspace', role: 'MEMBER' };
  const user = Storage.getUser() || { fullName: 'User' };
  const isAdmin = activeBiz.role === 'OWNER' || activeBiz.role === 'ADMIN';

  sidebarContainer.innerHTML = `
    <aside class="sidebar">
      <div class="sidebar-header">
        <a href="${base}/dashboard.html" class="brand-logo">
          <img src="${base}/assets/brand/favicon.svg" alt="VyaparOS" class="brand-logo-img" width="28" height="28" style="border-radius:6px; vertical-align:middle;">
          <span class="brand-logo-text">Vyapar<span style="color:#06b6d4;">OS</span></span>
        </a>
      </div>
      <div class="workspace-badge">
        <div class="workspace-name" title="${activeBiz.businessName}">${activeBiz.businessName}</div>
        <div class="workspace-role">${activeBiz.role}</div>
      </div>
      <ul class="nav-links">
        <li class="nav-item">
          <a href="${base}/dashboard.html" class="nav-link ${activeNavKey === 'dashboard' ? 'active' : ''}">
            <span>📊</span> Dashboard
          </a>
        </li>
        <li class="nav-item">
          <a href="${base}/appointments/index.html" class="nav-link ${activeNavKey === 'appointments' ? 'active' : ''}">
            <span>📅</span> Appointments
          </a>
        </li>
        <li class="nav-item">
          <a href="${base}/customers/index.html" class="nav-link ${activeNavKey === 'customers' ? 'active' : ''}">
            <span>👥</span> Customers
          </a>
        </li>
        <li class="nav-item">
          <a href="${base}/services/index.html" class="nav-link ${activeNavKey === 'services' ? 'active' : ''}">
            <span>✂️</span> Services
          </a>
        </li>
        <li class="nav-item">
          <a href="${base}/billing/index.html" class="nav-link ${activeNavKey === 'billing' ? 'active' : ''}">
            <span>🧾</span> Billing & Invoices
          </a>
        </li>
        <li class="nav-item">
          <a href="${base}/inventory/index.html" class="nav-link ${activeNavKey === 'inventory' ? 'active' : ''}">
            <span>📦</span> Products & Stock
          </a>
        </li>
        <li class="nav-item">
          <a href="${base}/attendance/index.html" class="nav-link ${activeNavKey === 'attendance' ? 'active' : ''}">
            <span>⏱️</span> Attendance
          </a>
        </li>
        ${isAdmin ? `
        <li class="nav-item">
          <a href="${base}/finance/index.html" class="nav-link ${activeNavKey === 'finance' ? 'active' : ''}">
            <span>💰</span> Expenses & Reports
          </a>
        </li>
        <li class="nav-item">
          <a href="${base}/staff/index.html" class="nav-link ${activeNavKey === 'staff' ? 'active' : ''}">
            <span>👔</span> Staff Team
          </a>
        </li>
        <li class="nav-item">
          <a href="${base}/settings/index.html" class="nav-link ${activeNavKey === 'settings' ? 'active' : ''}">
            <span>⚙️</span> Business Settings
          </a>
        </li>
        ` : ''}
      </ul>
      <div class="sidebar-footer">
        <div style="font-size: 0.85rem; margin-bottom: 8px; color: var(--text-muted);">
          Logged in as <strong style="color:var(--text-main);">${user.fullName || 'User'}</strong>
        </div>
        <button class="btn btn-secondary btn-sm" style="width:100%;" onclick="logoutUser()">
          Logout
        </button>
      </div>
    </aside>
  `;
}

function escapeHtml(str) {
  if (str === null || str === undefined) return '';
  return String(str)
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
    .replace(/"/g, '&quot;')
    .replace(/'/g, '&#039;');
}
window.escapeHtml = escapeHtml;

function logoutUser() {
  Storage.clear();
  window.location.href = `${getAppBasePath()}/login.html`;
}

window.API = {
  config: API_CONFIG,
  storage: Storage,
  request: apiRequest,
  showToast,
  checkAuthGuard,
  formatCurrency,
  isValidCurrencyCode,
  formatDate,
  formatDateTime,
  renderSidebar,
  logoutUser,
  escapeHtml,
  getBasePath: getAppBasePath,
  setApiUrl(url) {
    API_CONFIG.baseUrl = url;
    showToast(`API URL updated to: ${API_CONFIG.baseUrl}`, 'success');
  }
};
