(ns cassiopeia.destination.flatiron " .-. .   .-. .-. .-. .-. .-. .  .
	                              |-  |   |-|  |   |  |(  | | |\\|
                                      '   `-' ` '  '  `-' ' ' `-' ' ``"(:use [overtone.live][mud.core][mud.chords][cassiopeia.waves.synths][cassiopeia.samples][cassiopeia.engine.buffers][cassiopeia.dirt][cassiopeia.waves.buf-effects][cassiopeia.engine.expediency][cassiopeia.destination.flatiron.scores])(:require [mud.timing :as time][clojure.math.numeric-tower :as math][overtone.studio.fx :as fx] [cassiopeia.destination.flatiron.utils :as fl]))
(reset! splatter 500000.0)
(do (def master-vol 3.0) (volume master-vol))
(ctl-global-clock 0.0)

(do
      (defbufs 256 [df-b sd-attack-b sd-release-b sd-amp-b s-note-b])
      (pattern! sd-attack-b  [0.06 0.12 0.12 0.12])
      (pattern! sd-release-b [1.0  1.0 1.0 1.0])
      (pattern! sd-amp-b     [1.2  1.0 1.0 1.0])

      (def apeg-deep-melody-spair-chord-g (chord-synth general-purpose-assembly 4 :amp 0.0 :noise-level 0.05 :beat-trg-bus (:beat time/beat-1th) :beat-bus (:count time/beat-1th) :attack 0.1 :release 0.1))
      (def apeg-deep-melody-chord-g (chord-synth general-purpose-assembly 4 :amp 0.00 :saw-cutoff 2000 :wave 0 :attack 1.0 :release 5.0 :noise-level 0.05 :beat-trg-bus (:beat time/beat-1th) :beat-bus (:count time/beat-1th)))
      (def apeg-start (first (:bufs apeg-deep-melody-chord-g)))
      (def main-melody-chord-g (chord-synth general-purpose-assembly 3 :amp 0.0 :noise-level 0.05 :beat-trg-bus (:beat time/beat-2th) :beat-bus (:count time/beat-2th) :wave 1 :attack 1.0 :release 5.0))

      (defonce sd-g (group "slow deep chords"))
      (def slow-deep-chord-g (chord-synth general-purpose-assembly-pi 4 [:head sd-g] :saw-cutoff 300 :amp 0.0 :attack 0.1 :noise-level 0.05 :release 1.0 :wave 4 :beat-trg-bus (:beat time/beat-2th) :beat-bus (:count time/beat-2th) :attack 0.3 :release 6.0 :noise-level 0.05 :amp-buf sd-amp-b :release-buf sd-release-b :attack-buf sd-attack-b))

      (chord-pattern! main-melody-chord-g apeg-swell)
      (chord-pattern! slow-deep-chord-g chords-score)
      (chord-pattern! apeg-deep-melody-chord-g pinger-score)
      (chord-pattern! apeg-deep-melody-spair-chord-g pinger-score-spair)

      (do (defonce drums-g (group "drums")) (defonce drum-effects-g (group "drums effects for extra sweetness")) (defbufs 128 [bass-notes-buf bass-notes2-buf hats-buf kick-seq-buf white-seq-buf effects-seq-buf effects2-seq-buf bass-notes-buf]) (defonce hats-amp (buffer 256)) (defonce kick-amp (buffer 256)))
      (pattern! kick-amp  [1.5 1 1 1 1 1 1 1   1.1 1 1 1 1 1 1 1] (repeat 2 [1.2 1 1 1 1 1 1 1   1.1 1 1 1 1 1 1 1]) [1.2 1 1 1 1 1 1 1   1.2 1 1 1 1.2 1 1.3 1])
      (pattern! hats-amp  (repeat 3 [2 2 2 2 2.1 2 2 2   2 2 2 2 2 2 2 2]) [2 2 2 2 2.1 2 2 2   2 2 2.4 2 2.4 2 2 2])
      (pattern! bass-notes-buf
                             (repeat 8 (degrees [1] :minor :F1))
                             (repeat 2 (repeat 8 (degrees [1] :minor :F1)))
                             (repeat 2 (repeat 8 (degrees [3] :minor :F1)))
                             (repeat 2 (repeat 8 (degrees [3] :minor :F1)))
                             [(degrees [1 1 1 1  5 4 3 1] :minor :F1)])
    )




















(one-time-beat-trigger
 15 16
 (fn []
   (do
     (add-watch beat-tap :cell-color (fn [_ _ old new]
                                       (when (and (= old 0.0) (= 1.0 new))
                                         (reset! cell-dance-color (mod (+ @cell-dance-color 1.0) 100)))))

     (pattern! hats-buf     (repeat 3 [0 0 0 0  1 0 0 0  0 0 1 0  0 0 0 0])
                                      [0 0 0 0  1 0 0 0  0 0 1 0  1 0 0 0])
     (pattern! kick-seq-buf (repeat 3 [1 0 0 0  0 0 0 0  1 0 0 0  0 0 0 0])
                                      [1 0 0 0  0 0 0 0  1 0 0 0  1 0 1 0])

     (def white (whitenoise-hat [:head drums-g] :amp-buf hats-amp :seq-buf hats-buf :beat-bus (:count time/beat-1th) :beat-trg-bus (:beat time/beat-1th) :num-steps 16 :release 0.1 :attack 0.0 :beat-num 0))
     (ctl white :amp-buf hats-amp)
     (ctl white :attack 0.04 :release 0.01 :amp 1)
     (ctl white :attack 0.002 :release 0.04 :amp 2)

     (def kicker (space-kick2 [:head drums-g] :note-buf bass-notes-buf :seq-buf  kick-seq-buf :num-steps 16 :beat-num 0 :noise 0.05 :amp 4.2 :mod-index 0.1 :mod-freq 4.0 :mode-freq 0.2))
     (ctl kicker :amp-buf kick-amp :attack 0.0 :sustain 0.2 :amp 1.0)
     )))

;;START
(n-overtime! slow-deep-chord-g :amp 0.0 0.04 0.0008)

(one-time-beat-trigger
 126 128
 (fn [] ;;DARKER PROGRESSION
   (do
     (plain-space-organ :tone (/ (midi->hz (note :F1)) 2) :duration 3 :amp 0.45)
     (ctl apeg-deep-melody-chord-g :amp 0.00)
     (ctl drum-effects-g :amp 0.0)
     (ctl drums-g :amp 0.0)

     (chord-pattern slow-deep-chord-g dark-chords-score )
     (chord-pattern apeg-deep-melody-chord-g darker-pinger-score)
     )
   (doseq [s (:synths apeg-deep-melody-chord-g)]
     (ctl s :amp 0.00 :saw-cutoff 100 :wave 0 :attack 1.0 :release 5.0)
     (n-overtime! s :saw-cutoff 100 2000 50)
     (n-overtime! s :amp 0.00 0.24 0.03))

   (remove-watch beat-tap :cell-color)))

(ctl main-melody-chord-g :amp 0.6 :saw-cutoff)
(ctl apeg-deep-melody-chord-g :amp 0.228 :saw-cutoff  :wave 1)
(pattern! kick-seq-buf [1 0 0 0 1 0 0 0])
(ctl kicker :amp 0)
(ctl white :amp 0)

(do
  (overtime! circle-slice (* 0.5 Math/PI))(reset! cells-weight 4.0)
  (ctl main-melody-chord-g :amp 0.0)
  (ctl apeg-deep-melody-spair-chord-g :amp 0.00 :saw-cutoff 2000 :wave 2 :attack 1.0 :release 5.0)
  (n-overtime! apeg-deep-melody-spair-chord-g :amp 0 0.24 0.06)

  (chord-pattern apeg-deep-melody-spair-chord-g pinger-growth-score-spair)
  (ctl drum-effects-g :amp 0.3) (ctl drums-g :amp 1.0)

  (pattern! effects-seq-buf  (repeat 12 [1 0])  [1 0 0 0])
  (ctl apeg-deep-melody-chord-g :amp 0.3 :saw-cutoff 2600 :wave 0 :attack 1.0 :release 5.0)
  (def f (dulcet-fizzle :amp 2.0 :note-buf df-b))
  )

(do
  (ctl apeg-deep-melody-spair-chord-g :amp 0)
  (ctl-time apeg-deep-melody-spair-chord-g time/beat-2th)
  (ctl-time apeg-deep-melody-chord-g time/beat-2th)
  (ctl-time slow-deep-chord-g time/beat-1th)

  (chord-pattern slow-deep-chord-g pinger-score)

  (let [_ (pattern! sd-attack-b  [0.06 0.12 0.12 0.1])
        _ (pattern! sd-release-b [1.0 0.6 0.4 0.2])
        _ (pattern! sd-amp-b     [1.2 0.9 0.9 0.8])]
    (chord-pattern apeg-deep-melody-chord-g chords-score)))

(do
  (pattern! kick-seq-buf
            (repeat 3 (concat [1 0 0 0 1 0 0 0] [1 0 0 0 1 0 0 0]))
            [1 0 0 0 1 0 0 0] [1 0 0 0 1 0 1 0])
  (def f (dulcet-fizzle :amp 2.0 :note-buf df-b)))

(one-time-beat-trigger 126 128
                       (fn [& _]
                         (reset! cells-weight 5.0)
                         (ctl-time apeg-deep-melody-chord-g time/beat-1th)
                         (ctl-time apeg-deep-melody-spair-chord-g time/beat-1th)
                         (ctl-time slow-deep-chord-g time/beat-2th)

                         (one-time-beat-trigger
                          127 128
                          (fn [& _]
                            (def apeg-deep-melody-spair2-chord-g
                              (chord-synth general-purpose-assembly 4 :amp 0.0 :noise-level 0.05 :beat-trg-bus (:beat time/beat-1th) :beat-bus (:count time/beat-1th) :attack 0.1 :release 0.1))

                            (chord-pattern apeg-deep-melody-spair2-chord-g pinger-score-alternative)

                            (ctl-time apeg-deep-melody-chord-g time/beat-1th)
                            (ctl-time apeg-deep-melody-spair-chord-g time/beat-1th)
                            (ctl-time slow-deep-chord-g time/beat-2th)

                            (ctl main-melody-chord-g :amp 0.18)
                            (ctl apeg-deep-melody-spair2-chord-g :amp 0.18)
                            (chord-pattern main-melody-chord-g pinger-score-spair)
                            (n-overtime! apeg-deep-melody-spair2-chord-g :saw-cutoff 0.0 1000 50)
                            (n-overtime! apeg-deep-melody-spair-chord-g  :saw-cutoff 0.0 2600 50)
                            (n-overtime! main-melody-chord-g             :saw-cutoff 0.0 1000 50)

                            (chord-pattern apeg-deep-melody-spair-chord-g  pinger-growth-score-spair)
                            (chord-pattern apeg-deep-melody-chord-g        pinger-score-highlighted)

                            (let [_ (pattern! sd-attack-b  [0.06 0.12 0.12 0.12])
                                  _ (pattern! sd-release-b [1.0  1.0 1.0 1.0])
                                  _ (pattern! sd-amp-b     [1.2  1.0 1.0 1.0])]
                              (chord-pattern slow-deep-chord-g chords-score))
                            ))))

;;More fizzle
;;(doall (map #(n-overtime! % :saw-cutoff 2600.0 0 50) (:synths apeg-deep-melody-chord-g)))

(do
  (def main-melody2-chord-g (chord-synth general-purpose-assembly 3 :amp 0.0 :noise-level 0.05 :beat-trg-bus (:beat time/beat-2th) :beat-bus (:count time/beat-2th) :attack 0.1 :release 0.1))
  (chord-pattern main-melody2-chord-g apeg-swell)

  (reset! color 0.5)
  (chord-pattern main-melody2-chord-g  darker-pinger-score)
  (ctl main-melody2-chord-g :amp 0.18 :saw-cutoff 1000)
  (ctl main-melody-chord-g :saw-cutoff 300 :amp 0.18)
  (chord-pattern main-melody-chord-g apeg-swell))

(comment
  (do ;;init graphics
    (def beats (buffer->tap-lite kick-seq-buf (:count time/beat-1th) :measure 8))

    (defonce circle-count        (atom 4.0))
    (defonce color               (atom 0.1))
    (defonce circle-slice        (atom 8.0))
    (defonce circle-growth-speed (atom 0.1))
    (defonce circle-deform       (atom 1.0))
    (defonce circular-weight   (atom 0.0))
    (defonce population-weight (atom 0.0))
    (defonce cells-weight      (atom 0.0))
    (defonce nyc-weight        (atom 0.0))
    (defonce invert-color      (atom 1.0))
    (defonce cell-dance-weight (atom 1.0))
    (defonce splatter          (atom 50000.0))
    (def ibeat (atom {:synth beats :tap "beat"}))
    (def beat-tap (get-in (:synth @ibeat) [:taps (:tap @ibeat)]))
    (def cell-dance-color (atom 0.01))
    (add-watch beat-tap :cell-color
               (fn [_ _ old new]
                 (when (and (= old 0.0) (= 1.0 new))
                   (reset! cell-dance-color (mod (+ @cell-dance-color 1.0) 100)))))
    )

  ;;(kill beats)
  (start-graphics "resources/shaders/nyc.glsl"
           :textures [:overtone-audio :previous-frame
                      "resources/textures/tex16.png"]
           :user-data {"iGlobalBeatCount" (atom {:synth beats :tap "global-beat-count"})
                       "iBeat"            ibeat

                       "iColor" color
                       "iCircleCount" circle-count
                       "iHalfPi" circle-slice
                       "iInOutSpeed" circle-growth-speed
                       "iDeformCircles" circle-deform
                       "iCircularWeight"  circular-weight
                       "iPopulationWeight" population-weight
                       "iBouncingWeight"   cells-weight
                       "iNycWeight" nyc-weight
                       "iInvertColor" invert-color
                       "iCircleDanceWeight" cell-dance-weight
                       "iCircleDanceColor" cell-dance-color
                       "iDeath" fl/vol
                       "iSplatter" splatter
                       })
  (stop-graphics "resources/shaders/nyc.glsl")
  (stop-everything!)
  (stop)
)
