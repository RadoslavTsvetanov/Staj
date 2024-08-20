import React, { useRef, useEffect } from 'react'
import { useGLTF, useAnimations } from '@react-three/drei'
import * as THREE from 'three'

export function Model(props) {
  const group = useRef()
  const { nodes, materials, animations } = useGLTF('/landing.glb')
  const { actions } = useAnimations(animations, group)

  useEffect(() => {
    // Assuming there is only one animation, play it on loop
    const animationAction = actions[Object.keys(actions)[0]];
    if (animationAction) {
      animationAction.reset().play();
      animationAction.setLoop(THREE.LoopRepeat);
    }
  }, [actions]);

  return (
    <group ref={group} {...props} dispose={null}>
      <group name="Scene">
        <group name="Empty" position={[0, 10.203, -42.422]}>
          <group name="Plane007" position={[1.554, -1.824, 0.318]} />
          <group name="Plane" scale={1.076}>
            <mesh name="Circle" geometry={nodes.Circle.geometry} material={materials.White} />
            <mesh name="Circle_1" geometry={nodes.Circle_1.geometry} material={materials['Material.004']} />
            <mesh name="Circle_2" geometry={nodes.Circle_2.geometry} material={materials.Blue} />
            <mesh name="Circle007" geometry={nodes.Circle007.geometry} material={materials.White} position={[0, 0, 1.788]} rotation={[Math.PI / 2, 0, 0]}>
              <group name="Plane001" rotation={[-Math.PI / 2, 0, 0]} scale={1.246}>
                <mesh name="Plane001_1" geometry={nodes.Plane001_1.geometry} material={materials.White} />
                <mesh name="Plane001_2" geometry={nodes.Plane001_2.geometry} material={materials.Blue} />
              </group>
              <group name="Plane002" rotation={[-Math.PI / 2, 0, -2.094]} scale={1.246}>
                <mesh name="Plane001_1" geometry={nodes.Plane001_1.geometry} material={materials.White} />
                <mesh name="Plane001_2" geometry={nodes.Plane001_2.geometry} material={materials.Blue} />
              </group>
              <group name="Plane003_1" rotation={[-Math.PI / 2, 0, 2.094]} scale={1.246}>
                <mesh name="Plane001_1" geometry={nodes.Plane001_1.geometry} material={materials.White} />
                <mesh name="Plane001_2" geometry={nodes.Plane001_2.geometry} material={materials.Blue} />
              </group>
            </mesh>
            <group name="Circle001">
              <mesh name="Circle001_1" geometry={nodes.Circle001_1.geometry} material={materials.Blue} />
              <mesh name="Circle001_2" geometry={nodes.Circle001_2.geometry} material={materials.White} />
            </group>
            <group name="Circle002">
              <mesh name="Circle002_1" geometry={nodes.Circle002_1.geometry} material={materials.Blue} />
              <mesh name="Circle002_2" geometry={nodes.Circle002_2.geometry} material={materials.Blue} />
            </group>
            <mesh name="Circle003" geometry={nodes.Circle003.geometry} material={materials.Blue} />
            <group name="Circle004" position={[1.315, -1.315, 0.09]}>
              <mesh name="Circle004_1" geometry={nodes.Circle004_1.geometry} material={materials['Material.004']} />
              <mesh name="Circle004_2" geometry={nodes.Circle004_2.geometry} material={materials.White} />
            </group>
            <group name="Circle005" position={[0.002, 0.088, -5.815]} scale={0.482}>
              <mesh name="Circle004_1" geometry={nodes.Circle004_1.geometry} material={materials['Material.004']} />
              <mesh name="Circle004_2" geometry={nodes.Circle004_2.geometry} material={materials.White} />
            </group>
            {nodes.Circle006_1 && (
              <mesh name="Circle006_1" geometry={nodes.Circle006_1.geometry} material={materials.White} />
            )}
            <mesh name="Circle008" geometry={nodes.Circle008.geometry} material={materials.White} />
            <group name="Plane035" position={[0, 1.09, -0.297]} scale={[1, 1, 1.111]}>
              <mesh name="Plane_1" geometry={nodes.Plane_1.geometry} material={materials.White} />
              <mesh name="Plane_2" geometry={nodes.Plane_2.geometry} material={materials.Blue} />
            </group>
          </group>
        </group>
        <mesh name="Plane005" geometry={nodes.Plane005.geometry} material={materials['Material.001']} />
        <mesh name="Plane006" geometry={nodes.Plane006.geometry} material={materials['Material.002']} position={[-0.282, 0.163, -19.989]} scale={0.518} />
        <mesh name="Icosphere" geometry={nodes.Icosphere.geometry} material={materials['Material.004']} position={[0, -8.179, 0]} />
        <mesh name="Cube" geometry={nodes.Cube.geometry} material={materials['Material.005']} position={[-10.455, -1.353, -16.39]} rotation={[-1.21, -0.226, 0.92]} scale={1.983} />
        <mesh name="Cube001" geometry={nodes.Cube001.geometry} material={materials['Material.005']} position={[-10.5, -0.819, -24.971]} rotation={[-2.109, 0.704, 0.109]} scale={1.482} />
        <mesh name="Cube002" geometry={nodes.Cube002.geometry} material={materials['Material.005']} position={[6.118, -0.838, -27.634]} rotation={[-2.069, -0.044, -0.765]} scale={1.449} />
        <mesh name="Cube003" geometry={nodes.Cube003.geometry} material={materials['Material.005']} position={[13.84, 0, -11.008]} rotation={[-2.109, 0.704, 0.109]} scale={0.946} />
        <mesh name="Cube004" geometry={nodes.Cube004.geometry} material={materials['Material.005']} position={[-7.62, 0, 28.715]} rotation={[3.016, 0.299, -3.013]} scale={0.928} />
        <mesh name="Cube005" geometry={nodes.Cube005.geometry} material={materials['Material.005']} position={[2.38, -1.337, -33.942]} rotation={[-2.935, -0.513, -1.132]} scale={1.449} />
        <mesh name="Cube006" geometry={nodes.Cube006.geometry} material={materials['Material.005']} position={[-8.865, -0.975, -27.05]} rotation={[1.758, 1.278, 1.657]} scale={1.482} />
        <mesh name="Cube008" geometry={nodes.Cube008.geometry} material={materials['Material.005']} position={[-6.666, 0.028, 14.029]} rotation={[-0.155, -0.769, 2.997]} scale={0.918} />
        <mesh name="Cube009" geometry={nodes.Cube009.geometry} material={materials['Material.005']} position={[-7.831, -0.777, 12.92]} rotation={[-0.33, 0.177, 3.011]} scale={1.482} />
        <mesh name="Cube010" geometry={nodes.Cube010.geometry} material={materials['Material.005']} position={[-6.986, -0.176, 22.827]} rotation={[-0.155, -0.769, 2.997]} scale={0.557} />
        <mesh name="Cube007" geometry={nodes.Cube007.geometry} material={materials['Material.005']} position={[4.903, -0.176, 18.101]} rotation={[-3.024, -0.329, 0.001]} scale={0.803} />
      </group>
    </group>
  )
}

useGLTF.preload('/landing.glb')