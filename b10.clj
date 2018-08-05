(ns b10 (:use [overtone.live]) (:require [shadertone.tone :as t]) )

(do
  ;Global pulses
  (do
    (defonce root-trg-bus (control-bus)) ;; global metronome pulse
    (defonce root-cnt-bus (control-bus)) ;; global metronome count
    (defonce beat-trg-bus (control-bus)) ;; beat pulse (fraction of root)
    (defonce beat-cnt-bus (control-bus)) ;; beat count
    (def BEAT-FRACTION "Number of global pulses per beat" 30)
    )

  (do
    (defsynth root-trg [rate 100]
      (out:kr root-trg-bus (impulse:kr rate)))

    (defsynth root-cnt []
      (out:kr root-cnt-bus (pulse-count:kr (in:kr root-trg-bus))))

    (defsynth beat-trg [div BEAT-FRACTION]
      (out:kr beat-trg-bus (pulse-divider (in:kr root-trg-bus) div)))

    (defsynth beat-cnt []
      (out:kr beat-cnt-bus (pulse-count (in:kr beat-trg-bus)))))

  (do
    (def r-trg (root-trg))
    (def r-cnt (root-cnt [:after r-trg]))
    (def b-trg (beat-trg [:after r-trg]))
    (def b-cnt (beat-cnt [:after b-trg]))
    (ctl r-trg :rate 10)
    )

                                    ;Buses
  (do
                                    ;Control
    (defonce cbus1 (control-bus 1))
    (defonce cbus2 (control-bus 1))
    (defonce cbus3 (control-bus 1))
    (defonce cbus4 (control-bus 1))
    (defonce cbus5 (control-bus 1))
    (defonce cbus6 (control-bus 1))
    (defonce cbus7 (control-bus 1))
    (defonce cbus8 (control-bus 1))
    (defonce cbus9 (control-bus 1))
    (defonce cbus10 (control-bus 1))
    (defonce cbus11 (control-bus 1))
    (defonce cbus12 (control-bus 1))
    (defonce cbus13 (control-bus 1))
    (defonce cbus14 (control-bus 1))
    (defonce cbus15 (control-bus 1))
    (defonce cbus16 (control-bus 1))

    ;Audio
    (defonce abus1 (audio-bus))
    (defonce abus2 (audio-bus))
    (defonce abus3 (audio-bus))
    (defonce abus4 (audio-bus))
    (defonce abus5 (audio-bus))
    (defonce abus6 (audio-bus))
    (defonce abus7 (audio-bus))
    (defonce abus8 (audio-bus))
    (defonce abus9 (audio-bus))
    (defonce abus10 (audio-bus))
    (defonce abus11 (audio-bus))
    (defonce abus12 (audio-bus))
    (defonce abus13 (audio-bus))
    (defonce abus14 (audio-bus))
    (defonce abus15 (audio-bus))
    (defonce abus16 (audio-bus))


                                        ;Groups
    (defonce main-g (group "main bus"))
    (defonce early-g (group "early bus" :head main-g))
    (defonce later-g (group "late bus" :after early-g))
    )
                                        ;Buffers
    (defonce buffer-4-1 (buffer 4))
    (defonce buffer-4-2 (buffer 4))
    (defonce buffer-4-3 (buffer 4))
    (defonce buffer-4-4 (buffer 4))

    (defonce buffer-8-1 (buffer 8))
    (defonce buffer-8-2 (buffer 8))
    (defonce buffer-8-3 (buffer 8))
    (defonce buffer-8-4 (buffer 8))

    (defonce buffer-16-1 (buffer 16))
    (defonce buffer-16-2 (buffer 16))
    (defonce buffer-16-3 (buffer 16))
    (defonce buffer-16-4 (buffer 16))

    (defonce buffer-32-1 (buffer 32))
    (defonce buffer-32-2 (buffer 32))
    (defonce buffer-32-3 (buffer 32))
    (defonce buffer-32-4 (buffer 32))
  )

                                        ;Control synths
(defsynth sin-out [freq 1 out-control-bus 0]
  (out:kr out-control-bus (+ (in:kr freq) (sin-osc:kr (in:kr freq)))))

(def sin-out_1 (sin-out [:head early-g] cbus5 cbus6))

(kill sin-out_1)
                                    ;Synths
(defsynth sin-wave [amp 1 freq 22 phase 0 out-bus 0]
  (let [amp_in (in:kr amp)
        freq_in (in:kr freq)
        phase_in (in:kr phase)
        src (sin-osc freq_in phase_in 0)]
    (out out-bus (* amp_in src))))

(def sin-wave_1 (sin-wave [:head early-g] :amp cbus1 :freq cbus2 :phase cbus3 :out-bus abus1))

(def sin-wave_2 (sin-wave [:tail early-g] :amp cbus4 :freq cbus6 :phase 0 :out-bus abus2))

(ctl sin-wave_2 :phase cbus6 :freq cbus6)

(kill sin-wave_1)

(control-bus-set! cbus1 1)

(control-bus-set! cbus2 100)

(control-bus-set! cbus4 1.0)

(control-bus-set! cbus6 (* Math/PI 0.0))

(control-bus-set! cbus5 100)

(control-bus-get cbus6)

(pp-node-tree)



(kill 85)

(defsynth mixer [in-bus1 0 in-bus2 0 amp 1]
  (out 0 (pan2 (*  (+ (in in-bus1) (in in-bus2))  amp))))

(def mixer1 (mixer [:tail early-g] abus1 abus2 1))

(stop)

(kill 47)
(kill mixer1)
