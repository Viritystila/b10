(ns b10 (:use [overtone.live]) (:require [shadertone.tone :as t]) )


(do
  ;Global pulses
  (do
    (defonce root-trg-bus (control-bus)) ;; global metronome pulse
    (defonce root-cnt-bus (control-bus)) ;; global metronome count
    (defonce beat-trg-bus (control-bus)) ;; beat pulse (fraction of root)
    (defonce beat-cnt-bus (control-bus)) ;; beat count
    (def BEAT-FRACTION "Number of global pulses per beat" 4)
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
(do
  (defsynth sin-out [freq1 1 freq2 1 scaler 1 out-control-bus 0]
    (out:kr out-control-bus (+ (in:kr freq1) (* scaler (sin-osc:kr freq2)) )))

  (def sin-out_1 (sin-out [:tail early-g] :freq1 cbus5 :freq2 1 :scaler 5 :out-control-bus cbus6))

  (ctl sin-out_1 :freq2 2 :scaler 1)

                                        ;Synths
  (defsynth sin-wave [amp 1 freq 22 phase 0 out-bus 0]
    (let [amp_in (in:kr amp)
          freq_in (in:kr freq)
          phase_in (in:kr phase)
          src (sin-osc freq_in phase_in 0)]
      (out out-bus (* amp_in src))))

  (def sin-wave_1 (sin-wave [:tail early-g] :amp cbus1 :freq cbus2 :phase cbus3 :out-bus abus1))

  (def sin-wave_2 (sin-wave [:tail early-g] :amp cbus4 :freq cbus6 :phase 0 :out-bus abus2))

  (ctl sin-wave_2 :phase 0 :freq cbus6)

  (control-bus-set! cbus1 1)

  (control-bus-set! cbus2 50)

  (control-bus-set! cbus3 0)

  (control-bus-set! cbus4 0.5)

  (control-bus-set! cbus6 (* Math/PI 0.0))

  (control-bus-set! cbus5 55)

  (control-bus-get cbus6)

  (pp-node-tree)

  (ctl b-trg :div 2)
  (ctl r-trg :rate 12))


(do
  (defsynth buffSynth [out-bus 0 fraction 1 note-buf 0 beat-bus 0 beat-trg-bus 0
                       amp 1 attack 0.04 sustain 0.1 release 0.5
                       factor1 0.25 factor2 1.01 pnf 500]
    (let [cnt (in:kr beat-bus)
          trg (pulse-divider (in:kr beat-trg-bus) fraction)
          note (buf-rd:kr 1 note-buf cnt)
          freq (midicps note)
          ssas (pink-noise)
          freq (+ freq (* pnf ssas))
          vol (> note 0)
          pls (* vol trg)
          env (env-gen (perc :attack attack :sustain sustain :release release) :gate pls)
          src (lpf (mix [(saw (* factor1 freq)) (sin-osc (* factor2 freq))]))
          src2 (* amp env src)]
      (out:kr cbus7 note)
      (out out-bus (* src2))))


  (buffer-write! buffer-8-1 [50 30 80 20 70 0 0 0])

  (buffer-write! buffer-8-2 [100 90 80 70 60 50 40 30])

  (buffer-write! buffer-16-1 [80 100 60 70 50 60 40 50
                              50 40 60 50 70 50 70 50])

  (buffer-write! buffer-8-3 [55 45 50 45 50 45 50 45])

  (buffer-write! buffer-32-1 [55 45 44 45 55 45 55 45
                              60 50 60 50 60 50 60 50
                              80 100 90 60 70 60 50 40
                              50 40 40 50 50 40 40 50])

  (buffer-write! buffer-32-2 [45 35 55 65 55 65 75 85
                              65 65 65 65 55 55 55 55
                              85 65 45 35 55 75 95 105
                              100 95 90 85 80 75 70 65])

  (def buffSynth_1  (buffSynth [:head early-g] :out-bus abus3
                              :fraction 1
                              :note-buf buffer-8-2
                              :beat-trg-bus beat-trg-bus
                              :beat-bus beat-cnt-bus ))



  (ctl buffSynth_1
       :note-buf buffer-32-1
       :fraction 1
       :beat-trg-bus beat-trg-bus
       :beat-bus beat-cnt-bus
       :attack 0.04
       :release 0.8
       :factor1 0.25
       :factor2 1.01
       :amp 0.8
       :pnf 100)

 ; (kill buffSynth_1)

  (pp-node-tree)

  (buffer-write! buffer-16-2 [1 1 0 0 0 0 0 0
                              1 0 0 0 0 0 0 0])

  )

(do
  (defsynth dualPulse [out-bus 0 note 22 amp 1 fraction 1 in-bus 0 in-bus-ctr 0 beat-buf 0 attack 0.1 decay 0.1 sustain 0.2 release 1 del 0.0]
    (let [tr-in (pulse-divider (in:kr in-bus) fraction)
          ctr-in (in:kr in-bus-ctr)
          pulses (buf-rd:kr 1 beat-buf ctr-in)
          pls    (* tr-in pulses)
          env2 (env-gen (perc attack release) :gate pls)
          vol (> pulses 0)
          sp1 (sin-osc note)
          sp2 (sin-osc (* note 1.1))
          sp3 (sin-osc (* note 0.9))
          sp4 (* sp1 sp2 sp3 )]
      (out out-bus (clip2 (* amp sp4 env2) 1))))

  (def dualPulse_1 (dualPulse [:head early-g]
                              :out-bus abus5
                              :note 18
                              :amp 1
                              :fraction 1
                              :in-bus root-trg-bus
                              :in-bus-ctr root-cnt-bus
                              :beat-buf buffer-16-2
                              :attack 0.1
                              :decay 0.1
                              :sustain 0.1
                              :release 0.5
                              :del 0.0
                              ))

  (ctl dualPulse_1 :out-bus abus5 :fraction 1 :attack 0.1 :sustain 0.1 :release 0.5 :note 18 :amp 1)

  ;(kill dualPulse_1)

  (pp-node-tree)

  (buffer-write! buffer-16-3 [0 0 1 0 1 0 1 0
                              0 0 0 0 1 0 1 0])

  (buffer-write! buffer-32-4 [0 0 0 0 1 0 0 0
                              0 0 0 0 1 0 0 0
                              0 0 1 0 1 0 1 0
                              0 0 0 0 1 0 1 0])

  (defsynth snare [amp 30
                   fraction 2
                   del 0
                   in-trg-bus 0
                   in-bus-ctr 0
                   beat-buf 0
                   out-bus 0
                   del 0]
    (let [tr-in (pulse-divider (in:kr in-trg-bus) fraction)
          tr-in (t-delay:kr tr-in del)
          ctr-in (in:kr in-bus-ctr)
          pulses (buf-rd:kr 1 beat-buf ctr-in)
          pls (* tr-in pulses)
          env (linen (impulse 0 0.05) 0.01 0.01 0.05 :gate pls)
          snare (* 3 (pink-noise) (apply + (* (decay env [0.05 0.01]) [1 0.05])))
          snare (+ snare (bpf (* 4 snare) 2000))
          snare (clip2 snare 1)]
      (out out-bus (* amp snare env))))

  (def snare_1 (snare [:head early-g]
                      :amp 30
                      :fraction 1
                      :del 0
                      :in-trg-bus root-trg-bus
                      :in-bus-ctr root-cnt-bus
                      :beat-buf buffer-16-3
                      :out-bus abus4
                      :del 0))

  ;(kill snare_1)

  (ctl snare_1 :del 0.00 :beat-buf buffer-32-4)



  )

(defsynth pad [note 60 amp 0.07 attack 0.001 release 30.1]
  (let [freq (midicps note)
        env (env-gen (perc attack release) :action FREE)
        f-env (+ freq (* 10 freq (env-gen (perc 0.012 (- release 0.01)))))
        bfreq (/ freq 2)
        sig (apply - (concat (* 0.7 (sin-osc [bfreq (* 0.99 bfreq)])) (lpf (saw [freq (* freq 1.01)]) f-env)))
        ]
        (out 0 (pan2 (* amp env sig)))))

(def pad_1 (pad))

(pp-node-tree)



(do
  (defsynth mixer [in-bus1 0 amp1 1
                   in-bus2 0 amp2 1
                   in-bus3 0 amp3 1
                   in-bus4 0 amp4 1
                   in-bus5 0 amp5 1](let
                                        [in1 (in in-bus1)
                                         in2 (in in-bus2)
                                         in3 (in in-bus3)
                                         in4 (in in-bus4)
                                         in5 (in in-bus5)
                                         in1a (* amp1 in1)
                                         in2a (* amp2 in2)
                                         in3a (* amp3 in3)
                                         in4a (* amp4 in4)
                                         in5a (* amp5 in5)
                                         src (+ in1a in2a in3a in4a in5a)]
                                      (out 0 (pan2 src))))


  (def mixer1 (mixer [:tail early-g] abus1 1 abus2 1 abus3 1 abus4 1 abus5 1))

  (ctl mixer1
       :in-bus1 abus1
       :in-bus2 abus2
       :in-bus4 abus3
       :in-bus4 abus4
       :in-bus5 abus5
       :amp4 0.224
       :amp1 0.01
       :amp2 0.02
       :amp3 0.23
       :amp5 1)


  )


(kill 55)

(kill mixer1)


                                        ;Video
(def ch (t/get-cam-histogram 0 :red))

(def v1rh (t/get-video-histogram 0 :red))

(t/set-video-frame-limits 2  51000 52000)

ch

(t/toggle-analysis 0 false :histogram)

(def cb (control-bus-get cbus7))

(nth cb 0)


(t/start "./b10.glsl" :width 1920 :height 1080 :cams [0 1] :videos ["../videos/jkl.mp4" "../videos/metro.mp4" "../videos/spede.mp4"])

(add-watch ch :ch (fn [_ _ old new]
                    (let  [])
                    (t/set-dataArray-item 0 (nth (control-bus-get cbus7) 0))))

(remove-watch ch :ch)

(t/bufferSection 2 0 51000)

(t/set-video-fixed 2 :static)

(t/set-video-play 2)

(defonce root-cnt-bus-atom_1 (bus-monitor root-cnt-bus))

(defonce root-cnt-bus-atom_2 (bus-monitor root-cnt-bus))

(defonce beat-cnt-bus-atom_1 (bus-monitor beat-cnt-bus))

(control-bus-set! beat-cnt-bus 100)

(control-bus-get root-cnt-bus)

(bus-monitor beat-cnt-bus)

root-cnt-bus-atom_2

beat-cnt-bus-atom_1

(def frameset [2 3 200 100 50 30 40 50 60 40 50 60 70 100 120 110
               170 180 190 100 90 80 70 60 50 40 30 20 10 0 1 50 ])


(count frameset)

(def frameset [30 40])

(def frameset [120 130 140])

(add-watch root-cnt-bus-atom_2 :cnt (fn [_ _ old new]
                                    (let [])
                                      (t/set-dataArray-item 0 (Math/pow (nth (control-bus-get cbus7) 0) 1.05) )
                                    ;(t/set-fixed-buffer-index 2 :ff (nth (control-bus-get cbus7) 0))
                                    ;(t/set-fixed-buffer-index 2 :inc)
                                    (t/set-fixed-buffer-index 2 :ff (nth frameset (mod new (count frameset))))
                                      ;(t/set-fixed-buffer-index 2 :ff (int (+ 100 (* 100 (Math/sin (mod new 3.14))))))

                                      ))

(Math/pow 1 2)

(int -1)

(keys (:watches (bean root-cnt-bus-atom_2)))

(remove-watch root-cnt-bus-atom_2 :cnt)





(t/set-fixed-buffer-index 2 :ff 20)


(stop)
