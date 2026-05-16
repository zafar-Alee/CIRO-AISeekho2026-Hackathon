const API = '';
const SCENARIOS = {
  flood: {
    text: 'G-10 mein pani bhar gaya, gaariyan phans gayi hain. Sadkein doob gayi hain.',
    location: 'G-10, Islamabad',
    include_mock_signals: true,
    scenario: 'flood'
  },
  multi: {
    crises: [
      { text: 'G-10 mein pani bhar gaya, gaariyan phans gayi hain', location: 'G-10, Islamabad', include_mock_signals: true },
      { text: 'F-8 mein garmi ki wajah se log behosh ho rahe hain', location: 'F-8, Islamabad', include_mock_signals: true }
    ]
  },
  false_alarm: {
    text: 'G-10 sector mein paani hi paani hai, sab doob raha hai!',
    location: 'G-10, Islamabad',
    include_mock_signals: true,
    scenario: 'false_alarm'
  }
};

const STEP_ICONS = { 1: '📡', 2: '🔍', 3: '📊', 4: '🎯', 5: '🚀' };
const ACTION_ICONS = { TRAFFIC_REROUTE: '🚧', EMERGENCY_DISPATCH: '🚑', PUBLIC_ALERT: '📢', HOSPITAL_PREP: '🏥', UTILITY_ESCALATION: '⚡', MEDIA_UPDATE: '📰' };
let activeScenario = null;

/* ── Scenario Selection ── */
document.querySelectorAll('.scenario-card').forEach(card => {
  card.addEventListener('click', () => {
    document.querySelectorAll('.scenario-card').forEach(c => c.classList.remove('active'));
    card.classList.add('active');
    activeScenario = card.dataset.scenario;
    const s = SCENARIOS[activeScenario];
    if (activeScenario === 'multi') {
      document.getElementById('crisisText').value = s.crises.map((c, i) => `Crisis ${i + 1}: ${c.text}`).join('\n');
      document.getElementById('crisisLocation').value = s.crises.map(c => c.location).join(' + ');
    } else {
      document.getElementById('crisisText').value = s.text;
      document.getElementById('crisisLocation').value = s.location;
    }
  });
});

/* ── Run Pipeline ── */
document.getElementById('btnRun').addEventListener('click', runPipeline);

async function runPipeline() {
  if (!activeScenario) { alert('Select a scenario first!'); return; }
  const btn = document.getElementById('btnRun');
  btn.disabled = true;
  btn.textContent = 'Running Pipeline...';
  document.getElementById('resultsSection').classList.remove('visible');

  showPipeline();
  try {
    if (activeScenario === 'multi') {
      await injectFalseReport(false);
      const data = await callMulti();
      btn.textContent = 'Run CIRO Pipeline';
      btn.disabled = false;
      renderMultiResults(data);
    } else {
      if (activeScenario === 'false_alarm') await injectFalseReport(true);
      else await injectFalseReport(false);
      const data = await callSingle();
      btn.textContent = 'Run CIRO Pipeline';
      btn.disabled = false;
      renderSingleResults(data);
    }
  } catch (e) {
    btn.textContent = 'Run CIRO Pipeline';
    btn.disabled = false;
    renderError(e);
  }
}

async function injectFalseReport(inject) {
  if (inject) {
    await fetch(`${API}/mock/field-report`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ area: 'g10', report: 'Sirf broken water main hai, flood nahi', is_false_alarm: true })
    });
  }
}

async function callSingle() {
  for (let i = 1; i <= 5; i++) { activateStep(i); await delay(200); }
  const s = SCENARIOS[activeScenario];
  const res = await fetch(`${API}/analyze`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(s)
  });
  if (!res.ok) {
    const err = await res.json().catch(() => ({ detail: { message: `HTTP ${res.status}` } }));
    const detail = err.detail || err;
    const e = new Error(detail.message || detail.error || `HTTP ${res.status}`);
    e.status = res.status;
    e.detail = detail;
    throw e;
  }
  const data = await res.json();
  for (let i = 1; i <= 5; i++) completeStep(i);
  return data;
}

async function callMulti() {
  for (let i = 1; i <= 5; i++) { activateStep(i); await delay(200); }
  const res = await fetch(`${API}/analyze-multi`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(SCENARIOS.multi)
  });
  if (!res.ok) {
    const err = await res.json().catch(() => ({ detail: { message: `HTTP ${res.status}` } }));
    const detail = err.detail || err;
    const e = new Error(detail.message || detail.error || `HTTP ${res.status}`);
    e.status = res.status;
    e.detail = detail;
    throw e;
  }
  const data = await res.json();
  for (let i = 1; i <= 5; i++) completeStep(i);
  return data;
}

/* ── Pipeline Steps UI ── */
function showPipeline() {
  const sec = document.getElementById('pipelineSection');
  sec.classList.add('visible');
  document.querySelectorAll('.pipeline-step').forEach(s => { s.classList.remove('active', 'done'); });
}
function activateStep(n) { document.querySelector(`.pipeline-step[data-step="${n}"]`)?.classList.add('active'); }
function completeStep(n) {
  const el = document.querySelector(`.pipeline-step[data-step="${n}"]`);
  if (el) { el.classList.remove('active'); el.classList.add('done'); }
}

/* ── Render Single ── */
function renderSingleResults(d) {
  const sec = document.getElementById('resultsSection');
  const out = document.getElementById('resultsContent');
  const c = d.crisis;
  const sim = d.simulation;
  out.innerHTML = `
    ${resultHeader(d)}
    <div class="cards-grid">
      ${crisisCard(c)}
      ${simulationCard(sim)}
      ${actionsCard(d.actions)}
      ${signalsCard(d.signals_used)}
      ${messagesCard(d.stakeholder_messages)}
      ${reasoningCard(c.reasoning)}
    </div>`;
  sec.classList.add('visible');
  sec.scrollIntoView({ behavior: 'smooth', block: 'start' });
}

/* ── Render Multi ── */
function renderMultiResults(d) {
  const sec = document.getElementById('resultsSection');
  const out = document.getElementById('resultsContent');
  let html = `<div class="results-header"><div><h2>Multi-Crisis Response</h2></div>
    <div class="meta"><div class="meta-item">Processing: <strong>${d.total_processing_time_ms}ms</strong></div>
    <div class="meta-item">Incidents: <strong>${d.incidents.length}</strong></div></div></div>`;

  if (d.resource_trade_offs?.length) {
    html += `<div class="card card-full" style="margin-bottom:16px"><div class="card-title">⚖️ Resource Trade-Offs</div>`;
    d.resource_trade_offs.forEach(t => { html += `<div class="tradeoff-item">${esc(t)}</div>`; });
    html += `</div>`;
  }

  d.incidents.forEach((inc, i) => {
    const c = inc.crisis;
    const sim = inc.simulation;
    html += `<h3 style="margin:24px 0 12px;font-size:16px;color:var(--accent-light)">Incident ${i + 1}: ${esc(inc.incident_id)}</h3>
      <div class="cards-grid">
        ${crisisCard(c)}
        ${simulationCard(sim)}
        ${actionsCard(inc.actions)}
        ${messagesCard(inc.stakeholder_messages)}
      </div>`;
  });
  out.innerHTML = html;
  sec.classList.add('visible');
  sec.scrollIntoView({ behavior: 'smooth', block: 'start' });
}

/* ── Card Builders ── */
function resultHeader(d) {
  return `<div class="results-header"><div><h2>Pipeline Results</h2></div>
    <div class="meta">
      <div class="meta-item">Incident: <strong>${esc(d.incident_id)}</strong></div>
      <div class="meta-item">Time: <strong>${d.processing_time_ms}ms</strong></div>
      <div class="meta-item">Trace: <strong>${esc(d.agent_trace_id)}</strong></div>
    </div></div>`;
}

function crisisCard(c) {
  return `<div class="card">
    <div class="card-title">🚨 Crisis Classification</div>
    <div class="crisis-header">
      <div class="crisis-type">${esc(c.type.replace(/_/g, ' '))}</div>
      <div class="crisis-badge severity-${c.severity}">${c.severity}</div>
    </div>
    <div class="crisis-stats">
      <div class="stat"><div class="stat-value">${c.confidence}</div><div class="stat-label">Confidence</div></div>
      <div class="stat"><div class="stat-value">${fmt(c.affected_population)}</div><div class="stat-label">Population</div></div>
      <div class="stat"><div class="stat-value">${c.affected_radius_km}km</div><div class="stat-label">Radius</div></div>
      <div class="stat"><div class="stat-value">${c.expected_duration_hours}h</div><div class="stat-label">Duration</div></div>
    </div>
    ${c.conflicting_signals ? '<div style="margin-top:16px;padding:10px 14px;background:var(--orange-bg);border-radius:8px;font-size:13px;color:var(--orange)">⚠️ Conflicting signals detected — possible false positive</div>' : ''}
  </div>`;
}

function simulationCard(sim) {
  const b = sim.before, a = sim.after;
  const improv = a.response_time_before_min > 0 ? Math.round((1 - a.response_time_after_min / a.response_time_before_min) * 100) : 0;
  return `<div class="card">
    <div class="card-title">📈 Simulation</div>
    <div class="sim-compare">
      <div class="sim-box"><h4>Before CIRO</h4>
        <div class="sim-row"><span class="label">Congestion</span><span>${b.congestion_level}/10</span></div>
        <div class="sim-row"><span class="label">Response Time</span><span>${a.response_time_before_min} min</span></div>
        <div class="sim-row"><span class="label">Tickets</span><span>${b.emergency_tickets.length}</span></div>
      </div>
      <div class="sim-arrow">→</div>
      <div class="sim-box"><h4>After CIRO</h4>
        <div class="sim-row"><span class="label">Congestion</span><span>${a.congestion_level}/10</span></div>
        <div class="sim-row"><span class="label">Response Time</span><span>${a.response_time_after_min} min</span></div>
        <div class="sim-row"><span class="label">Tickets</span><span>${a.emergency_tickets.length}</span></div>
      </div>
    </div>
    <div style="text-align:center;margin-top:16px" class="improvement">${improv}% faster response</div>
  </div>`;
}

function actionsCard(actions) {
  let items = '';
  actions.forEach(a => {
    items += `<div class="action-item">
      <div class="action-icon">${ACTION_ICONS[a.type] || '📋'}</div>
      <div class="action-body">
        <div class="action-title">${esc(a.description)}</div>
        <div class="action-entity">${esc(a.entity)} · Priority ${a.priority}</div>
      </div>
      <div class="action-id">${esc(a.action_id)}</div>
    </div>`;
  });
  return `<div class="card"><div class="card-title">🎯 Response Actions (${actions.length})</div>${items}</div>`;
}

function signalsCard(signals) {
  let items = '';
  (signals || []).forEach(s => {
    items += `<div class="signal-item">
      <div class="signal-source">${esc(s.source)}</div>
      <div class="signal-text">${esc(s.text.substring(0, 120))}</div>
      <div class="signal-cred">${s.credibility}</div>
    </div>`;
  });
  return `<div class="card"><div class="card-title">📡 Signals Used (${(signals||[]).length})</div>${items}</div>`;
}

function messagesCard(m) {
  if (!m) return '';
  const msgs = [
    ['Public', m.public], ['Hospital', m.hospital],
    ['Traffic Police', m.traffic_police], ['Utility', m.utility], ['Media', m.media]
  ];
  let items = '';
  msgs.forEach(([to, text]) => {
    if (text) items += `<div class="msg-item"><div class="msg-to">${to}</div><div class="msg-text">${esc(text)}</div></div>`;
  });
  return `<div class="card"><div class="card-title">💬 Stakeholder Messages</div>${items}</div>`;
}

function reasoningCard(text) {
  if (!text) return '';
  return `<div class="card card-full"><div class="card-title">🧠 AI Reasoning</div><div class="reasoning-box">${esc(text)}</div></div>`;
}

/* ── Error Rendering ── */
function renderError(e) {
  const sec = document.getElementById('resultsSection');
  const out = document.getElementById('resultsContent');
  const is503 = e.status === 503;
  const detail = e.detail || {};
  out.innerHTML = `
    <div class="card card-full" style="border-left:4px solid ${is503 ? 'var(--orange)' : 'var(--red)'}">
      <div class="card-title">${is503 ? '🤖 AI Service Unavailable (503)' : '❌ Pipeline Error'}</div>
      <div class="reasoning-box" style="border-left-color:${is503 ? 'var(--orange)' : 'var(--red)'}">
        <strong>${esc(e.message)}</strong>
        ${detail.error ? '<br><br><span style="color:var(--text-muted);font-size:13px">' + esc(detail.error) + '</span>' : ''}
        ${is503 ? '<br><br><span style="color:var(--orange)">The Gemini API key may be exhausted or invalid. Check your .env file and API quota at <a href="https://ai.dev/rate-limit" target="_blank" style="color:var(--accent-light)">ai.dev/rate-limit</a></span>' : ''}
        ${detail.trace_id ? '<br><br><span style="font-family:JetBrains Mono;font-size:12px;color:var(--text-muted)">Trace: ' + esc(detail.trace_id) + '</span>' : ''}
      </div>
    </div>`;
  sec.classList.add('visible');
  sec.scrollIntoView({ behavior: 'smooth', block: 'start' });
}

/* ── Helpers ── */
function esc(s) { const d = document.createElement('div'); d.textContent = s || ''; return d.innerHTML; }
function fmt(n) { return n >= 1000 ? (n / 1000).toFixed(0) + 'K' : n; }
function delay(ms) { return new Promise(r => setTimeout(r, ms)); }
